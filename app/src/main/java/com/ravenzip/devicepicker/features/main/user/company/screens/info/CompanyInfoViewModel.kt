package com.ravenzip.devicepicker.features.main.user.company.screens.info

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.common.enums.EmployeePositionEnum
import com.ravenzip.devicepicker.common.model.UiEvent
import com.ravenzip.devicepicker.common.model.UiState
import com.ravenzip.devicepicker.common.repositories.AuthRepository
import com.ravenzip.devicepicker.common.repositories.CompanyRepository
import com.ravenzip.devicepicker.common.repositories.SharedRepository
import com.ravenzip.devicepicker.common.repositories.UserRepository
import com.ravenzip.devicepicker.features.main.user.company.model.Company
import com.ravenzip.devicepicker.features.main.user.company.model.CompanyDeleteRequest
import com.ravenzip.devicepicker.features.main.user.company.model.EmployeeWithDevice
import com.ravenzip.devicepicker.navigation.models.CompanyGraph
import com.ravenzip.kotlinflowextended.functions.dematerialize
import com.ravenzip.kotlinflowextended.functions.filterErrorNotification
import com.ravenzip.kotlinflowextended.functions.filterNextNotification
import com.ravenzip.kotlinflowextended.models.FlowNotification
import com.ravenzip.workshop.data.SpinnerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CompanyInfoViewModel
@Inject
constructor(
    authRepository: AuthRepository,
    private val companyRepository: CompanyRepository,
    private val userRepository: UserRepository,
    sharedRepository: SharedRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _companyUid = savedStateHandle.getStateFlow("uid", "")

    val navigateTo = MutableSharedFlow<String>()
    val navigateBackToParent = MutableSharedFlow<Unit>()
    val leaveCompany = MutableSharedFlow<CompanyDeleteRequest>()

    val snackBarHostState = SnackbarHostState()

    private val _loadCompany =
        _companyUid
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
            companyDeleteRequest.employeePosition == EmployeePositionEnum.Employee
        }

    private val _acceptDelete =
        _calculateEmployeesBeforeLeave.filter { companyDeleteRequest ->
            companyDeleteRequest.employees.isEmpty()
        }

    private val _rejectLeave =
        _calculateEmployeesBeforeLeave.filter { companyDeleteRequest ->
            companyDeleteRequest.employeePosition == EmployeePositionEnum.Leader &&
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
        merge(_leaveCompanySuccess, _deleteCompanySuccess)
            .map { "" }
            .flatMapLatest { uid -> userRepository.updateCompanyUid(uid) }
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _updateCompanyUidInUserSuccess =
        _updateCompanyUidInUserComplete
            .filterNextNotification()
            .dematerialize()
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _updateCompanyUidInUserError =
        _updateCompanyUidInUserComplete
            .filterErrorNotification()
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _updateUserDataComplete =
        _updateCompanyUidInUserSuccess
            .flatMapLatest { sharedRepository.loadUserData() }
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _updateUserDataSuccess = _updateUserDataComplete.filterNextNotification()

    private val _updateUserDataError =
        _updateUserDataComplete
            .filterErrorNotification()
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _snackBarErrorMessage =
        merge(
            merge(_updateCompanyUidInUserError).map { errorNotification ->
                errorNotification.error.message
                    ?: "При сохранении данных о компании произошла ошибка"
            },
            merge(_loadCompanyError).map { errorNotification ->
                errorNotification.error.message
                    ?: "При получении данных о компании произошла ошибка"
            },
            _rejectLeave.map {
                "Невозможно покинуть компанию, т.к. вы являетесь директором и в компании более 1 участника"
            },
            merge(_leaveCompanyError, _deleteCompanyError).map { errorNotification ->
                errorNotification.error.message ?: "При выходе из компании произошла ошибка"
            },
            _updateUserDataError.map { errorNotification ->
                errorNotification.error.message
                    ?: "При обновлении данных пользователя произошла ошибка"
            },
        )

    val uiState =
        merge(
                _loadCompanySuccess.map { company -> UiState.Success(company) },
                _loadCompanyError.map { errorNotification ->
                    val errorMessage =
                        errorNotification.error.message
                            ?: "При получении данных о компании произошла ошибка"

                    return@map UiState.Error(errorMessage)
                },
            )
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UiState.Loading("Загрузка..."),
            )

    private val _spinnerIsLoading =
        merge(
                merge(leaveCompany).map { 1 },
                merge(
                        _updateCompanyUidInUserError,
                        _rejectLeave,
                        _leaveCompanyError,
                        _deleteCompanyError,
                        _updateUserDataError,
                    )
                    .map { -1 },
            )
            .scan(0) { acc, value -> acc + value }
            .map { counter -> counter > 0 }

    val spinner =
        _spinnerIsLoading
            .map { isLoading -> SpinnerState(isLoading = isLoading, text = "Выход из компании...") }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = SpinnerState(isLoading = false, text = ""),
            )

    val uiEvent =
        merge(
            navigateTo.map { route -> UiEvent.Navigate.ByRoute(route) },
            _updateUserDataSuccess.map {
                UiEvent.Navigate.WithoutBackStack(CompanyGraph.COMPANY_ROOT)
            },
            navigateBackToParent.map { UiEvent.Navigate.Parent },
            _snackBarErrorMessage.map { errorMessage -> UiEvent.ShowSnackBar.Error(errorMessage) },
        )

    private val _hasCompany =
        uiState
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
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = EmployeePositionEnum.Unknown,
            )

    val employeesCount =
        _hasCompany
            .map { company -> company.data.employees.count() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = 0,
            )

    // Вот тут начинается часть для экранов с сотрудниками и устройствами компании
    val company =
        _hasCompany
            .map { uiState -> uiState.data }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = Company(),
            )

    val employeesWithActiveDevices =
        company
            .map { company ->
                company.employees.map { employee ->
                    EmployeeWithDevice(
                        employee,
                        company.devices.filter { device ->
                            device.uid in employee.devices && device.active
                        },
                    )
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList(),
            )

    val devices =
        company
            .map { company -> company.devices }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList(),
            )

    val devicesIsEmpty =
        company
            .map { company -> company.devices.isEmpty() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = true,
            )
}
