package com.ravenzip.devicepicker.features.auth.login

import android.util.Patterns.PHONE
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.common.enums.AuthTypeEnum
import com.ravenzip.devicepicker.common.model.result.Result
import com.ravenzip.devicepicker.common.repositories.AuthRepository
import com.ravenzip.devicepicker.common.utils.extension.showError
import com.ravenzip.devicepicker.features.auth.common.AuthForm
import com.ravenzip.workshop.forms.Validators
import com.ravenzip.workshop.forms.control.FormControl
import com.ravenzip.workshop.forms.group.FormGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)

    val form =
        FormGroup(
            AuthForm(
                authType = FormControl(initialValue = AuthTypeEnum.EMAIL),
                email =
                    FormControl(
                        initialValue = "",
                        validators =
                            listOf(
                                { value -> Validators.required(value) },
                                { value -> Validators.email(value) },
                            ),
                    ),
                password =
                    FormControl(
                        initialValue = "",
                        validators =
                            listOf(
                                { value -> Validators.required(value) },
                                { value -> Validators.minLength(value, 6) },
                            ),
                    ),
                phone =
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
                code = FormControl(initialValue = ""),
            )
        )

    val isLoading = _isLoading.asStateFlow()
    val snackBarHostState = SnackbarHostState()

    fun logInWithEmailAndPassword(navigateToHomeScreen: () -> Unit) {
        viewModelScope.launch {
            if (form.controls.email.isInvalid || form.controls.password.isInvalid) {
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
                    form.controls.email.value,
                    form.controls.password.value,
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
        form.controls.authType.valueChanges
            .onEach {
                form.controls.email.reset()
                form.controls.password.reset()
                form.controls.phone.reset()
                form.controls.code.reset()
            }
            .launchIn(viewModelScope)
    }
}
