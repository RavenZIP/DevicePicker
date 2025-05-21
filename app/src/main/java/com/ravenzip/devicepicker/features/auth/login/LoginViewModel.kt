package com.ravenzip.devicepicker.features.auth.login

import android.util.Patterns.PHONE
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.common.enums.AuthVariantsEnum
import com.ravenzip.devicepicker.common.model.result.Result
import com.ravenzip.devicepicker.common.repositories.AuthRepository
import com.ravenzip.devicepicker.common.utils.extension.showError
import com.ravenzip.workshop.forms.Validators
import com.ravenzip.workshop.forms.component.TextFieldComponent
import com.ravenzip.workshop.forms.control.FormControl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)

    val authOptionsControl = FormControl(initialValue = AuthVariantsEnum.EMAIL)

    val emailComponent =
        TextFieldComponent(
            control =
                FormControl(
                    initialValue = "",
                    validators =
                        listOf(
                            { value -> Validators.required(value) },
                            { value -> Validators.email(value) },
                        ),
                ),
            scope = viewModelScope,
        )

    val passwordComponent =
        TextFieldComponent(
            control =
                FormControl(
                    initialValue = "",
                    validators =
                        listOf(
                            { value -> Validators.required(value) },
                            { value -> Validators.minLength(value, 6) },
                        ),
                ),
            scope = viewModelScope,
        )

    val phoneState =
        TextFieldComponent(
            control =
                FormControl(
                    initialValue = "",
                    validators =
                        listOf(
                            { value -> Validators.required(value) },
                            { value ->
                                if (!PHONE.matcher(value).matches())
                                    "Введен некорректный номер телефона"
                                else null
                            },
                        ),
                ),
            scope = viewModelScope,
        )

    val codeState =
        TextFieldComponent(control = FormControl(initialValue = ""), scope = viewModelScope)

    val isLoading = _isLoading.asStateFlow()
    val snackBarHostState = SnackbarHostState()

    fun logInWithEmailAndPassword(navigateToHomeScreen: () -> Unit) {
        viewModelScope.launch {
            if (emailComponent.control.isInvalid || passwordComponent.control.isInvalid) {
                snackBarHostState.showError("Проверьте правильность заполнения полей")
                return@launch
            }

            _isLoading.update { true }

            val reloadResult = authRepository.reloadUser()
            if (reloadResult is Result.Error) {
                _isLoading.update { false }
                snackBarHostState.showError(reloadResult.message)
                return@launch
            }

            val authResult =
                authRepository.logInUserWithEmail(
                    emailComponent.control.value,
                    passwordComponent.control.value,
                )
            if (authResult is Result.Error) {
                _isLoading.update { false }
                snackBarHostState.showError(authResult.message)
                return@launch
            }

            _isLoading.update { false }
            navigateToHomeScreen()
        }
    }

    init {
        viewModelScope.launch {
            authOptionsControl.valueChanges.collect {
                emailComponent.control.reset()
                passwordComponent.control.reset()
                phoneState.control.reset()
                codeState.control.reset()
            }
        }
    }
}
