package com.ravenzip.devicepicker.features.main.user.company.screens.join

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.common.enums.EmployeePositionEnum
import com.ravenzip.devicepicker.common.model.UiEvent
import com.ravenzip.devicepicker.common.repositories.CompanyRepository
import com.ravenzip.devicepicker.common.repositories.SharedRepository
import com.ravenzip.devicepicker.features.main.user.company.enum.CompanySettingsEnum
import com.ravenzip.devicepicker.features.main.user.company.model.Company
import com.ravenzip.devicepicker.navigation.models.CompanyGraph
import com.ravenzip.kotlinflowextended.functions.dematerialize
import com.ravenzip.kotlinflowextended.functions.filterErrorNotification
import com.ravenzip.kotlinflowextended.functions.filterNextNotification
import com.ravenzip.workshop.data.SpinnerState
import com.ravenzip.workshop.forms.Validators
import com.ravenzip.workshop.forms.control.FormControl
import com.ravenzip.workshop.forms.group.FormGroup
import com.ravenzip.workshop.forms.state.DropDownTextFieldState
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

    val form =
        FormGroup(
            JoinToCompanyForm(
                company = FormControl(initialValue = Company()),
                leader = FormControl(initialValue = "", disable = true),
                address = FormControl(initialValue = "", disable = true),
                code =
                    FormControl(
                        initialValue = "",
                        validators = listOf { value -> Validators.required(value) },
                    ),
            )
        )

    val companiesControlState =
        DropDownTextFieldState(source = _companies, sourceView = { item -> item.name })

    private val _acceptJoinToCompany =
        joinToCompany.filter { form.controls.code.value == form.controls.company.value.code }

    private val _rejectJoinToCompany =
        joinToCompany.filter { form.controls.code.value != form.controls.company.value.code }

    private val _acceptToJoinCompanyAfterApprove =
        _acceptJoinToCompany
            .map {
                form.controls.company.value.settings.firstOrNull { setting ->
                    setting.code == CompanySettingsEnum.JOIN_AFTER_APPROVE.ordinal
                }
            }
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _acceptJoinToCompanyWithApprove =
        _acceptToJoinCompanyAfterApprove.filter { setting -> setting?.active == true }

    private val _acceptJoinToCompanyWithoutApprove =
        _acceptToJoinCompanyAfterApprove.filter { setting -> setting?.active == false }

    private val _sendRequestToJoinComplete =
        _acceptJoinToCompanyWithApprove.flatMapLatest {
            companyRepository.addRequestToJoinInCompany(form.controls.company.value.uid)
        }

    private val _sendRequestToJoinSuccess =
        _sendRequestToJoinComplete
            .filterNextNotification()
            .dematerialize()
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _sendRequestToJoinError =
        _sendRequestToJoinComplete
            .filterErrorNotification()
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _joinToCompanyComplete =
        _acceptJoinToCompanyWithoutApprove
            .flatMapLatest { sharedRepository.userFullName }
            .flatMapLatest { userFullName ->
                companyRepository.joinToCompany(form.controls.company.value.uid, userFullName)
            }
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _joinToCompanySuccess =
        _joinToCompanyComplete
            .filterNextNotification()
            .dematerialize()
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _joinToCompanyError =
        _joinToCompanyComplete
            .filterErrorNotification()
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _updateUserDataComplete =
        merge(_joinToCompanySuccess, _sendRequestToJoinSuccess)
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
                errorNotification.error.message ?: "При вступлении в компанию произошла ошибка"
            },
            _sendRequestToJoinError.map { errorNotification ->
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
                merge(
                        _joinToCompanyError,
                        _rejectJoinToCompany,
                        _updateUserDataError,
                        _sendRequestToJoinError,
                    )
                    .map { -1 },
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
                .flatMapLatest { merge(_joinToCompanySuccess, _sendRequestToJoinSuccess) }
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

        form.controls.company.valueChanges
            .onEach { company ->
                val leader =
                    company.employees.firstOrNull { employee ->
                        employee.position == EmployeePositionEnum.Leader
                    }

                form.controls.leader.setValue(leader?.name ?: "")
                form.controls.address.setValue(company.address)

                if (company.uid.isNotEmpty()) {
                    form.controls.code.enable()
                } else {
                    form.controls.code.disable()
                }
            }
            .launchIn(viewModelScope)
    }
}
