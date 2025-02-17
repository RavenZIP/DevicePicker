package com.ravenzip.devicepicker.viewmodels.user

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.constants.enums.EmployeePosition
import com.ravenzip.devicepicker.model.User.Companion.fullName
import com.ravenzip.devicepicker.model.company.Company
import com.ravenzip.devicepicker.repositories.AuthRepository
import com.ravenzip.devicepicker.repositories.CompanyRepository
import com.ravenzip.devicepicker.repositories.SharedRepository
import com.ravenzip.devicepicker.repositories.UserRepository
import com.ravenzip.devicepicker.state.UiEvent
import com.ravenzip.devicepicker.state.UiState
import com.ravenzip.devicepicker.ui.screens.main.user.company.SpinnerState
import com.ravenzip.devicepicker.ui.screens.main.user.company.enums.CompanyScreenActionsEnum
import com.ravenzip.devicepicker.ui.screens.main.user.company.enums.CompanyScreenTypesEnum
import com.ravenzip.kotlinflowextended.functions.dematerialize
import com.ravenzip.kotlinflowextended.functions.filterErrorNotification
import com.ravenzip.kotlinflowextended.functions.filterNextNotification
import com.ravenzip.kotlinflowextended.models.FlowNotification
import com.ravenzip.workshop.forms.state.FormState
import com.ravenzip.workshop.forms.state.special.DropDownTextFieldState
import com.ravenzip.workshop.forms.state.special.TextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// TODO подумать над разбиением на разные viewModel
@HiltViewModel
class CompanyViewModel
@Inject
constructor(
    sharedRepository: SharedRepository,
    authRepository: AuthRepository,
    private val companyRepository: CompanyRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    val createCompany = MutableSharedFlow<Unit>()
    val joinToCompany = MutableSharedFlow<Unit>()

    val companyUid =
        sharedRepository.userDataFlow
            .map { userData -> userData.companyUid }
            .stateIn(scope = viewModelScope, started = SharingStarted.Lazily, initialValue = "")

    val companies = mutableStateListOf<Company>()

    val snackBarHostState = SnackbarHostState()

    val companyScreenTypeState = FormState(CompanyScreenActionsEnum.CREATE_COMPANY)

    val companyNameState = TextFieldState(initialValue = "")
    val companyDescriptionState = TextFieldState(initialValue = "")
    val companyAddressState = TextFieldState(initialValue = "")

    val companyState =
        DropDownTextFieldState(
            initialValue = Company(),
            items = companies,
            itemsView = { item -> item.name },
        )
    val companyLeaderState = TextFieldState(initialValue = "", disable = true)

    private val _createCompanyComplete =
        createCompany
            .flatMapLatest {
                companyRepository.addCompany(
                    companyNameState.value,
                    companyDescriptionState.value,
                    companyAddressState.value,
                    sharedRepository.userData.fullName,
                )
            }
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _createCompanySuccess =
        _createCompanyComplete
            .filterNextNotification()
            .dematerialize()
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _updateCompanyUidInUserComplete =
        _createCompanySuccess
            .flatMapLatest { uid -> userRepository.updateCompanyUid(uid) }
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _updateCompanyUidInUserSuccess =
        _updateCompanyUidInUserComplete
            .filterNextNotification()
            .dematerialize()
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _updateCompanyUidInUserError =
        _updateCompanyUidInUserComplete.filterErrorNotification()

    private val _createCompanyError = _createCompanyComplete.filterErrorNotification()

    private val _joinToCompanyComplete =
        joinToCompany
            .flatMapLatest { companyRepository.addRequestToJoinInCompany(companyState.value.uid) }
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _joinToCompanySuccess =
        _joinToCompanyComplete
            .filterNextNotification()
            .dematerialize()
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _joinToCompanyError = _joinToCompanyComplete.filterErrorNotification()

    private val _loadCompanyAfterCreateOrJoinComplete =
        merge(_updateCompanyUidInUserSuccess, _joinToCompanySuccess)
            .flatMapLatest { companyUid -> companyRepository.getCompanyByUid(companyUid) }
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _loadCompanySuccess =
        _loadCompanyAfterCreateOrJoinComplete
            .filterNextNotification()
            .dematerialize()
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _loadCompanyError = _loadCompanyAfterCreateOrJoinComplete.filterErrorNotification()

    private val _firstLoadCompanyComplete =
        companyUid.flatMapLatest { uid ->
            if (uid.isEmpty()) flowOf(FlowNotification.Next(Company()))
            else companyRepository.getCompanyByUid(uid)
        }

    private val _firstLoadCompanySuccess =
        _firstLoadCompanyComplete.filterNextNotification().dematerialize()

    private val _firstLoadCompanyError =
        _loadCompanyAfterCreateOrJoinComplete.filterErrorNotification()

    // TODO скорее всего не хватает еще одного UiState.Loading
    val companyStateFlow =
        merge(
                merge(_firstLoadCompanySuccess, _loadCompanySuccess).map { company ->
                    UiState.Success(company)
                },
                merge(_firstLoadCompanyError, _loadCompanyError).map { errorNotification ->
                    val errorMessage =
                        errorNotification.error.message
                            ?: "При получении данных о компании произошла ошибка"

                    return@map UiState.Error(errorMessage)
                },
            )
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = UiState.Loading("Загрузка..."),
            )

    val currentUserPositionInCompany =
        companyStateFlow
            .filterIsInstance<UiState.Success<Company>>()
            .map { companyState ->
                companyState.data.employees
                    .first { employee -> employee.uid == authRepository.firebaseUser?.uid }
                    .position
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = EmployeePosition.Unknown,
            )

    val employeesCount =
        companyStateFlow
            .filterIsInstance<UiState.Success<Company>>()
            .map { company -> company.data.employees.count() }
            .stateIn(scope = viewModelScope, started = SharingStarted.Lazily, initialValue = 0)

    private val _spinnerIsLoading =
        merge(
                merge(createCompany, joinToCompany).map { 1 },
                merge(
                        _createCompanyError,
                        _updateCompanyUidInUserError,
                        _joinToCompanyError,
                        _updateCompanyUidInUserSuccess,
                        _updateCompanyUidInUserError,
                    )
                    .map { -1 },
            )
            .scan(0) { acc, value -> acc + value }
            .map { counter -> counter > 0 }

    private val _spinnerText =
        merge(
            createCompany.map { "Создание компании" },
            joinToCompany.map { "Отправка заявки на вступление" },
        )

    val spinner =
        _spinnerIsLoading
            .combine(_spinnerText) { isLoading, text -> SpinnerState(isLoading, text) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = SpinnerState(isLoading = false, text = ""),
            )

    val screenModeByUserState =
        companyUid
            .map { companyUid ->
                if (authRepository.isAnonymousUser) CompanyScreenTypesEnum.ANONYMOUS
                else if (companyUid.isEmpty()) CompanyScreenTypesEnum.NOT_REGISTERED
                else CompanyScreenTypesEnum.REGISTERED
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = CompanyScreenTypesEnum.ANONYMOUS,
            )

    private val _showSuccessSnackBar =
        merge(
            _updateCompanyUidInUserSuccess.map { "Компания была успешно создана" },
            _joinToCompanySuccess.map { "Запрос на вступление в организацию отправлен" },
            _loadCompanySuccess.map { "Данные о компании были успешно загружены" },
        )

    private val _showErrorSnackBar =
        merge(
            merge(_createCompanyError, _updateCompanyUidInUserError).map { errorNotification ->
                errorNotification.error.message
                    ?: "При сохранении данных о компании произошла ошибка"
            },
            _joinToCompanyError.map { errorNotification ->
                errorNotification.error.message
                    ?: "При отправке запроса на вступление в компанию произошла ошибка"
            },
            merge(_firstLoadCompanyError, _loadCompanyError).map { errorNotification ->
                errorNotification.error.message
                    ?: "При получении данных о компании произошла ошибка"
            },
        )

    val uiEvent =
        merge(
            _showSuccessSnackBar.map { message -> UiEvent.ShowSnackBar.Success(message) },
            _showErrorSnackBar.map { message -> UiEvent.ShowSnackBar.Error(message) },
        )

    init {
        viewModelScope.launch {
            companyScreenTypeState.valueChanges.collect { type ->
                companyNameState.reset()
                companyDescriptionState.reset()
                companyAddressState.reset()
                companyState.reset()
                companyLeaderState.reset()

                if (type == CompanyScreenActionsEnum.CREATE_COMPANY) {
                    companyAddressState.enable()
                } else {
                    companyAddressState.disable()
                }
            }
        }

        viewModelScope.launch {
            companyScreenTypeState.valueChanges
                .filter { type -> type == CompanyScreenActionsEnum.JOIN_COMPANY }
                .flatMapLatest { companyRepository.getCompanies() }
                .filterNextNotification()
                .dematerialize()
                .collect { companiesFromDatabase ->
                    companies.clear()
                    companies.addAll(companiesFromDatabase)
                }
        }

        viewModelScope.launch {
            companyState.valueChanges
                .filter { company -> company.uid.isNotEmpty() }
                .collect { company ->
                    val leader =
                        company.employees.first { employee ->
                            employee.position === EmployeePosition.Leader
                        }

                    companyLeaderState.setValue(leader.name)
                    companyAddressState.setValue(company.address)
                }
        }

        viewModelScope.launch {
            _loadCompanySuccess.flatMapLatest { sharedRepository.getUserData() }.collect()
        }
    }
}
