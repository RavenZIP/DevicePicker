package com.ravenzip.devicepicker.ui.screens.main.user.company.viewmodel

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.constants.enums.EmployeePosition
import com.ravenzip.devicepicker.model.company.Company
import com.ravenzip.devicepicker.navigation.models.CompanyGraph
import com.ravenzip.devicepicker.repositories.CompanyRepository
import com.ravenzip.devicepicker.repositories.SharedRepository
import com.ravenzip.devicepicker.state.UiEvent
import com.ravenzip.devicepicker.ui.screens.main.user.company.SpinnerState
import com.ravenzip.kotlinflowextended.functions.dematerialize
import com.ravenzip.kotlinflowextended.functions.filterErrorNotification
import com.ravenzip.kotlinflowextended.functions.filterNextNotification
import com.ravenzip.workshop.forms.Validators
import com.ravenzip.workshop.forms.state.special.DropDownTextFieldState
import com.ravenzip.workshop.forms.state.special.TextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class JoinToCompanyViewModel
@Inject
constructor(companyRepository: CompanyRepository, sharedRepository: SharedRepository) :
    ViewModel() {
    val joinToCompany = MutableSharedFlow<Unit>()
    val navigateBack = MutableSharedFlow<Unit>()
    val navigateBackToParent = MutableSharedFlow<Unit>()

    val snackBarHostState = SnackbarHostState()

    private val _loadCompanies =
        companyRepository
            .getCompanies()
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _loadCompaniesSuccess = _loadCompanies.filterNextNotification().dematerialize()

    private val _loadCompaniesError = _loadCompanies.filterErrorNotification()

    private val _companies = mutableStateListOf<Company>()

    val companyState =
        DropDownTextFieldState(
            initialValue = Company(),
            items = _companies,
            itemsView = { item -> item.name },
        )
    val companyLeaderState = TextFieldState(initialValue = "", disable = true)
    val companyAddressState = TextFieldState(initialValue = "", disable = true)
    val companyCodeState =
        TextFieldState(
            initialValue = "",
            validators = listOf { value -> Validators.required(value) },
        )

    private val _acceptJoinToCompany =
        joinToCompany.filter { companyCodeState.value == companyState.value.code }

    private val _rejectJoinToCompany =
        joinToCompany.filter { companyCodeState.value != companyState.value.code }

    private val _joinToCompanyComplete =
        _acceptJoinToCompany
            .flatMapLatest { companyRepository.addRequestToJoinInCompany(companyState.value.uid) }
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _joinToCompanySuccess =
        _joinToCompanyComplete
            .filterNextNotification()
            .dematerialize()
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _joinToCompanyError = _joinToCompanyComplete.filterErrorNotification()

    private val _updateUserDataComplete =
        _joinToCompanySuccess
            .flatMapLatest { sharedRepository.loadUserData() }
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _updateUserDataSuccess = _updateUserDataComplete.filterNextNotification()

    private val _updateUserDataError =
        _updateUserDataComplete
            .filterErrorNotification()
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _snackBarErrorMessage =
        merge(
            _loadCompaniesError.map { errorNotification ->
                errorNotification.error.message ?: "При загрузке списка компаний произошла ошибка"
            },
            _rejectJoinToCompany.map { "Введен неверный код организации" },
            _joinToCompanyError.map { errorNotification ->
                errorNotification.error.message
                    ?: "При отправке запроса на вступление в компанию произошла ошибка"
            },
            _updateUserDataError.map { errorNotification ->
                errorNotification.error.message
                    ?: "При обновлении данных пользователя произошла ошибка"
            },
        )

    private val _spinnerIsLoading =
        merge(
                merge(joinToCompany).map { 1 },
                merge(_joinToCompanyError, _rejectJoinToCompany, _updateUserDataError).map { -1 },
            )
            .scan(0) { acc, value -> acc + value }
            .map { counter -> counter > 0 }

    val spinner =
        _spinnerIsLoading
            .map { isLoading ->
                SpinnerState(isLoading = isLoading, text = "Отправка заявки на вступление...")
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = SpinnerState(isLoading = false, text = ""),
            )

    val uiEvent =
        merge(
            _updateUserDataSuccess
                .flatMapLatest { _joinToCompanySuccess }
                .map { companyUid ->
                    UiEvent.Navigate.ByRoute("${CompanyGraph.COMPANY_INFO}/${companyUid}")
                },
            navigateBack.map { UiEvent.Navigate.Back },
            navigateBackToParent.map { UiEvent.Navigate.Parent },
            _snackBarErrorMessage.map { errorMessage -> UiEvent.ShowSnackBar.Error(errorMessage) },
        )

    init {
        _loadCompaniesSuccess
            .onEach { companies -> _companies.addAll(companies) }
            .launchIn(viewModelScope)

        companyState.valueChanges
            .onEach { company ->
                val leader =
                    company.employees.firstOrNull { employee ->
                        employee.position == EmployeePosition.Leader
                    }

                companyLeaderState.setValue(leader?.name ?: "")
                companyAddressState.setValue(company.address)

                if (company.uid.isNotEmpty()) {
                    companyCodeState.enable()
                } else {
                    companyCodeState.disable()
                }
            }
            .launchIn(viewModelScope)
    }
}
