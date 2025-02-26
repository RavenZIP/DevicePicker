package com.ravenzip.devicepicker.viewmodels.user

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.constants.enums.EmployeePosition
import com.ravenzip.devicepicker.model.CompanyDeleteRequest
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
    val leaveCompany = MutableSharedFlow<CompanyDeleteRequest>()

    val companyUid =
        sharedRepository.userDataFlow
            .map { userData -> userData.companyUid }
            .stateIn(scope = viewModelScope, started = SharingStarted.Lazily, initialValue = "")

    private val _companies = mutableStateListOf<Company>()

    val snackBarHostState = SnackbarHostState()

    val companyScreenTypeState = FormState(CompanyScreenActionsEnum.CREATE_COMPANY)

    val companyNameState = TextFieldState(initialValue = "")
    val companyDescriptionState = TextFieldState(initialValue = "")
    val companyAddressState = TextFieldState(initialValue = "")
    val companyCodeState = TextFieldState(initialValue = "")

    val companyState =
        DropDownTextFieldState(
            initialValue = Company(),
            items = _companies,
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
                    companyCodeState.value,
                    sharedRepository.userData.fullName,
                )
            }
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _createCompanySuccess =
        _createCompanyComplete
            .filterNextNotification()
            .dematerialize()
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _calculateEmployeesBeforeLeave =
        leaveCompany
            .map { companyDeleteRequest ->
                return@map if (companyDeleteRequest.employees.count() == 1) {
                    companyDeleteRequest.copy(employees = emptyList())
                } else {
                    val employees = companyDeleteRequest.employees.toMutableList()
                    employees.removeIf { it.uid == authRepository.firebaseUser?.uid }
                    companyDeleteRequest.copy(employees = employees)
                }
            }
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _acceptLeave =
        _calculateEmployeesBeforeLeave.filter { companyDeleteRequest ->
            companyDeleteRequest.employeePosition == EmployeePosition.Employee
        }

    private val _acceptDelete =
        _calculateEmployeesBeforeLeave.filter { companyDeleteRequest ->
            companyDeleteRequest.employees.isEmpty()
        }

    private val _rejectLeave =
        _calculateEmployeesBeforeLeave.filter { companyDeleteRequest ->
            companyDeleteRequest.employeePosition == EmployeePosition.Leader &&
                companyDeleteRequest.employees.isNotEmpty()
        }

    private val _leaveCompanyComplete =
        _acceptLeave.flatMapLatest { companyDeleteRequest ->
            companyRepository.leaveCompany(
                companyDeleteRequest.companyUid,
                companyDeleteRequest.employees,
            )
        }

    private val _leaveCompanySuccess = _leaveCompanyComplete.filterNextNotification()

    private val _leaveCompanyError =
        _leaveCompanyComplete
            .filterErrorNotification()
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _deleteCompanyComplete =
        _acceptDelete
            .flatMapLatest { companyDeleteRequest ->
                companyRepository.deleteCompany(companyDeleteRequest.companyUid)
            }
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _deleteCompanySuccess = _deleteCompanyComplete.filterNextNotification()

    private val _deleteCompanyError =
        _deleteCompanyComplete
            .filterErrorNotification()
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _updateCompanyUidInUserComplete =
        merge(_createCompanySuccess, merge(_leaveCompanySuccess, _deleteCompanySuccess).map { "" })
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

    private val _loadCompany =
        merge(_updateCompanyUidInUserSuccess, _joinToCompanySuccess)
            .flatMapLatest { companyUid ->
                if (companyUid.isEmpty()) flowOf(FlowNotification.Next(Company()))
                else companyRepository.getCompanyByUid(companyUid)
            }
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _loadCompanySuccess =
        _loadCompany
            .filterNextNotification()
            .dematerialize()
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _loadCompanyError = _loadCompany.filterErrorNotification()

    private val _firstLoadCompanyComplete =
        companyUid
            .flatMapLatest { uid ->
                if (uid.isEmpty()) flowOf(FlowNotification.Next(Company()))
                else companyRepository.getCompanyByUid(uid)
            }
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _firstLoadCompanySuccess =
        _firstLoadCompanyComplete.filterNextNotification().dematerialize()

    private val _firstLoadCompanyError =
        _firstLoadCompanyComplete
            .filterErrorNotification()
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

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

    private val _hasCompany =
        companyStateFlow
            .filterIsInstance<UiState.Success<Company>>()
            .filter { companyState -> companyState.data.uid.isNotEmpty() }
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    val currentUserPositionInCompany =
        _hasCompany
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
        _hasCompany
            .map { company -> company.data.employees.count() }
            .stateIn(scope = viewModelScope, started = SharingStarted.Lazily, initialValue = 0)

    private val _spinnerIsLoading =
        merge(
                merge(createCompany, joinToCompany, leaveCompany).map { 1 },
                merge(
                        _createCompanyError,
                        _updateCompanyUidInUserError,
                        _joinToCompanyError,
                        _updateCompanyUidInUserSuccess,
                        _updateCompanyUidInUserError,
                        _rejectLeave,
                        _leaveCompanyError,
                        _deleteCompanyError,
                        _rejectJoinToCompany,
                    )
                    .map { -1 },
            )
            .scan(0) { acc, value -> acc + value }
            .map { counter -> counter > 0 }

    private val _spinnerText =
        merge(
            createCompany.map { "Создание компании" },
            joinToCompany.map { "Отправка заявки на вступление" },
            leaveCompany.map { "Выход из компании" },
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
            _rejectLeave.map {
                "Невозможно покинуть компанию, т.к. вы являетесь директором и в компании более 1 участника"
            },
            merge(_leaveCompanyError, _deleteCompanyError).map { errorNotification ->
                errorNotification.error.message ?: "При выходе из компании произошла ошибка"
            },
            _rejectJoinToCompany.map { "Введен неверный код организации" },
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
                companyCodeState.reset()

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
                    _companies.clear()
                    _companies.addAll(companiesFromDatabase)
                }
        }

        viewModelScope.launch {
            companyState.valueChanges
                .filter { company -> company.uid.isNotEmpty() }
                .collect { company ->
                    val leader =
                        company.employees.first { employee ->
                            employee.position == EmployeePosition.Leader
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
