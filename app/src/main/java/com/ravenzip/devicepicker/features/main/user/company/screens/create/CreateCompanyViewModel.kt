package com.ravenzip.devicepicker.features.main.user.company.screens.create

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.common.model.UiEvent
import com.ravenzip.devicepicker.common.repositories.CompanyRepository
import com.ravenzip.devicepicker.common.repositories.SharedRepository
import com.ravenzip.devicepicker.common.repositories.UserRepository
import com.ravenzip.devicepicker.navigation.models.CompanyGraph
import com.ravenzip.kotlinflowextended.functions.dematerialize
import com.ravenzip.kotlinflowextended.functions.filterErrorNotification
import com.ravenzip.kotlinflowextended.functions.filterNextNotification
import com.ravenzip.workshop.data.SpinnerState
import com.ravenzip.workshop.forms.Validators
import com.ravenzip.workshop.forms.component.TextFieldComponent
import com.ravenzip.workshop.forms.control.FormControl
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

    val companyNameComponent =
        TextFieldComponent(
            FormControl(
                initialValue = "",
                validators = listOf { value -> Validators.required(value) },
            ),
            scope = viewModelScope,
        )
    val companyDescriptionComponent =
        TextFieldComponent(
            FormControl(
                initialValue = "",
                validators = listOf { value -> Validators.required(value) },
            ),
            scope = viewModelScope,
        )
    val companyAddressComponent =
        TextFieldComponent(
            FormControl(
                initialValue = "",
                validators = listOf { value -> Validators.required(value) },
            ),
            scope = viewModelScope,
        )
    val companyCodeComponent =
        TextFieldComponent(
            FormControl(
                initialValue = "",
                validators = listOf { value -> Validators.required(value) },
            ),
            scope = viewModelScope,
        )

    private val _createCompanyComplete =
        createCompany
            .flatMapLatest { sharedRepository.userFullName }
            .flatMapLatest { userFullName ->
                companyRepository.addCompany(
                    companyNameComponent.control.value,
                    companyDescriptionComponent.control.value,
                    companyAddressComponent.control.value,
                    companyCodeComponent.control.value,
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

    private val _updateUserDataSuccess = _updateUserDataComplete.filterNextNotification()

    private val _updateUserDataError =
        _updateUserDataComplete
            .filterErrorNotification()
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
                started = SharingStarted.WhileSubscribed(5000),
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
