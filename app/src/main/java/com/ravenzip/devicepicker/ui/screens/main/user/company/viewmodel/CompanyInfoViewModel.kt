package com.ravenzip.devicepicker.ui.screens.main.user.company.viewmodel

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.constants.enums.EmployeePosition
import com.ravenzip.devicepicker.model.CompanyDeleteRequest
import com.ravenzip.devicepicker.model.company.Company
import com.ravenzip.devicepicker.navigation.models.CompanyGraph
import com.ravenzip.devicepicker.repositories.AuthRepository
import com.ravenzip.devicepicker.repositories.CompanyRepository
import com.ravenzip.devicepicker.repositories.SharedRepository
import com.ravenzip.devicepicker.repositories.UserRepository
import com.ravenzip.devicepicker.state.UiEvent
import com.ravenzip.devicepicker.state.UiState
import com.ravenzip.devicepicker.ui.screens.main.user.company.SpinnerState
import com.ravenzip.kotlinflowextended.functions.dematerialize
import com.ravenzip.kotlinflowextended.functions.filterErrorNotification
import com.ravenzip.kotlinflowextended.functions.filterNextNotification
import com.ravenzip.kotlinflowextended.models.FlowNotification
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
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

    val company =
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
                started = SharingStarted.Lazily,
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
                started = SharingStarted.Lazily,
                initialValue = SpinnerState(isLoading = false, text = ""),
            )

    val uiEvent =
        merge(
            merge(
                    _updateUserDataSuccess
                        .flatMapLatest { _updateCompanyUidInUserSuccess }
                        .map { companyUid -> "${CompanyGraph.COMPANY_INFO}/${companyUid}" },
                    navigateTo,
                )
                .map { route -> UiEvent.Navigate.ByRoute(route) },
            _snackBarErrorMessage.map { errorMessage -> UiEvent.ShowSnackBar.Error(errorMessage) },
        )

    private val _hasCompany =
        company
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
}
