package com.ravenzip.devicepicker.ui.screens.main.user.company.viewmodel

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.navigation.models.CompanyGraph
import com.ravenzip.devicepicker.repositories.CompanyRepository
import com.ravenzip.devicepicker.repositories.SharedRepository
import com.ravenzip.devicepicker.repositories.UserRepository
import com.ravenzip.devicepicker.state.UiEvent
import com.ravenzip.devicepicker.ui.screens.main.user.company.SpinnerState
import com.ravenzip.kotlinflowextended.functions.dematerialize
import com.ravenzip.kotlinflowextended.functions.filterErrorNotification
import com.ravenzip.kotlinflowextended.functions.filterNextNotification
import com.ravenzip.workshop.forms.Validators
import com.ravenzip.workshop.forms.state.special.TextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class CreateCompanyViewModel
@Inject
constructor(
    companyRepository: CompanyRepository,
    sharedRepository: SharedRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    val createCompany = MutableSharedFlow<Unit>()
    val navigateBack = MutableSharedFlow<Unit>()
    val navigateBackToParent = MutableSharedFlow<Unit>()

    val snackBarHostState = SnackbarHostState()

    val companyNameState =
        TextFieldState(
            initialValue = "",
            validators = listOf { value -> Validators.required(value) },
        )
    val companyDescriptionState =
        TextFieldState(
            initialValue = "",
            validators = listOf { value -> Validators.required(value) },
        )
    val companyAddressState =
        TextFieldState(
            initialValue = "",
            validators = listOf { value -> Validators.required(value) },
        )
    val companyCodeState =
        TextFieldState(
            initialValue = "",
            validators = listOf { value -> Validators.required(value) },
        )

    private val _createCompanyComplete =
        createCompany
            .flatMapLatest { sharedRepository.userFullName }
            .flatMapLatest { userFullName ->
                companyRepository.addCompany(
                    companyNameState.value,
                    companyDescriptionState.value,
                    companyAddressState.value,
                    companyCodeState.value,
                    userFullName,
                )
            }
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _createCompanySuccess =
        _createCompanyComplete
            .filterNextNotification()
            .dematerialize()
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _createCompanyError = _createCompanyComplete.filterErrorNotification()

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

    private val _updateUserDataComplete =
        _updateCompanyUidInUserSuccess
            .flatMapLatest { sharedRepository.loadUserData() }
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _updateUserDataSuccess =
        _updateUserDataComplete.filterNextNotification().onEach {
            println("_updateUserDataSuccess")
        }

    private val _updateUserDataError =
        _updateUserDataComplete
            .filterErrorNotification()
            .onEach { println("_updateUserDataError") }
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _snackBarErrorMessage =
        merge(
            merge(_createCompanyError, _updateCompanyUidInUserError).map { errorNotification ->
                errorNotification.error.message
                    ?: "При сохранении данных о компании произошла ошибка"
            },
            _updateUserDataError.map { errorNotification ->
                errorNotification.error.message
                    ?: "При обновлении данных пользователя произошла ошибка"
            },
        )

    private val _spinnerIsLoading =
        merge(
                merge(createCompany).map { 1 },
                merge(_createCompanyError, _updateCompanyUidInUserError, _updateUserDataError).map {
                    -1
                },
            )
            .scan(0) { acc, value -> acc + value }
            .map { counter -> counter > 0 }

    val spinner =
        _spinnerIsLoading
            .map { isLoading -> SpinnerState(isLoading = isLoading, text = "Создание компании...") }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = SpinnerState(isLoading = false, text = ""),
            )

    val uiEvent =
        merge(
            _updateUserDataSuccess
                .flatMapLatest { _updateCompanyUidInUserSuccess }
                .map { companyUid -> "${CompanyGraph.COMPANY_INFO}/${companyUid}" }
                .map { route -> UiEvent.Navigate.ByRoute(route) },
            navigateBack.map { UiEvent.Navigate.Back },
            navigateBackToParent.map { UiEvent.Navigate.Parent },
            _snackBarErrorMessage.map { errorMessage -> UiEvent.ShowSnackBar.Error(errorMessage) },
        )
}
