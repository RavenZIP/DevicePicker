package com.ravenzip.devicepicker.features.auth.registration

import android.util.Patterns.PHONE
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.common.enums.AuthCardEnum
import com.ravenzip.devicepicker.common.enums.AuthTypeEnum
import com.ravenzip.devicepicker.common.model.result.Result
import com.ravenzip.devicepicker.common.repositories.AuthRepository
import com.ravenzip.devicepicker.common.repositories.UserRepository
import com.ravenzip.devicepicker.common.utils.extension.showError
import com.ravenzip.devicepicker.common.utils.extension.showWarning
import com.ravenzip.devicepicker.features.auth.common.AuthForm
import com.ravenzip.workshop.forms.Validators
import com.ravenzip.workshop.forms.control.FormControl
import com.ravenzip.workshop.forms.group.FormGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel
@Inject
constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    private val _spinnerText = MutableStateFlow("Регистрация...")

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
    val spinnerText = _spinnerText.asStateFlow()
    val snackBarHostState = SnackbarHostState()

    fun registrationWithEmailAndPassword(navigateToHomeScreen: () -> Unit) {
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

            _spinnerText.update { "Регистрация..." }

            val authResult =
                authRepository.createUserWithEmail(
                    form.controls.email.value,
                    form.controls.password.value,
                )
            if (authResult is Result.Error) {
                _isLoading.update { false }
                snackBarHostState.showError(authResult.message)
                return@launch
            }

            _spinnerText.update { "Отправка письма с подтверждением..." }

            val messageResult = authRepository.sendEmailVerification()
            if (messageResult is Result.Error) {
                _isLoading.update { false }
                snackBarHostState.showWarning(messageResult.message)
                authRepository.deleteAccount() // TODO проверять падение запроса
                return@launch
            }

            _spinnerText.update { "Ожидание подтверждения электронной почты..." }

            val emailIsNotVerified = checkEmailVerification()
            // Если пользователь не успел подтвердить электронную почту,
            // то удаляем аккаунт
            if (emailIsNotVerified) {
                _isLoading.update { false }
                authRepository.deleteAccount()
                return@launch
            }

            // TODO добавить обработку в случае ошибки при создании пользователя
            userRepository.createUserData()
            _isLoading.update { false }
            navigateToHomeScreen()
        }
    }

    fun getSelectedOptionDescription(selectedOption: AuthTypeEnum): String =
        when (selectedOption) {
            AuthTypeEnum.EMAIL -> AuthCardEnum.REGISTER_WITH_EMAIL.value
            AuthTypeEnum.PHONE -> AuthCardEnum.REGISTER_WITH_PHONE.value
        }

    private suspend fun checkEmailVerification(): Boolean {
        var timer = 25 // Время, за которое необходимо зарегистрироваться пользователю

        while (timer > 0) {
            if (authRepository.isEmailVerified()) {
                timer = -1
            } else {
                timer -= 1
                delay(1000)
            }
        }

        return timer == 0
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
