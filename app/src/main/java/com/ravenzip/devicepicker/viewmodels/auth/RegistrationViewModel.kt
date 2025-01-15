package com.ravenzip.devicepicker.viewmodels.auth

import android.util.Patterns.PHONE
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.ravenzip.devicepicker.constants.enums.AuthCardEnum
import com.ravenzip.devicepicker.constants.enums.AuthVariantsEnum
import com.ravenzip.devicepicker.extensions.functions.showError
import com.ravenzip.devicepicker.extensions.functions.showWarning
import com.ravenzip.devicepicker.model.result.Result
import com.ravenzip.devicepicker.repositories.AuthRepository
import com.ravenzip.devicepicker.repositories.UserRepository
import com.ravenzip.workshop.forms.Validators
import com.ravenzip.workshop.forms.state.FormState
import com.ravenzip.workshop.forms.state.special.TextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class RegistrationViewModel
@Inject
constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    private val _spinnerText = MutableStateFlow("Регистрация...")

    val authOptionsState = FormState(initialValue = AuthVariantsEnum.EMAIL)

    val emailState =
        TextFieldState(
            initialValue = "",
            validators =
                listOf(
                    { value -> Validators.required(value) },
                    { value -> Validators.email(value) },
                ),
        )

    val passwordState =
        TextFieldState(
            initialValue = "",
            validators =
                listOf(
                    { value -> Validators.required(value) },
                    { value -> Validators.minLength(value, 6) },
                ),
        )

    val phoneState =
        TextFieldState(
            initialValue = "",
            validators =
                listOf(
                    { value -> Validators.required(value) },
                    { value ->
                        if (!PHONE.matcher(value).matches()) "Введен некорректный номер телефона"
                        else null
                    },
                ),
        )

    val codeState = TextFieldState(initialValue = "")

    val isLoading = _isLoading.asStateFlow()
    val spinnerText = _spinnerText.asStateFlow()
    val snackBarHostState = SnackbarHostState()

    /**
     * Текущий пользователь firebase
     *
     * @return [FirebaseUser] или null
     */
    private val firebaseUser: FirebaseUser?
        get() = authRepository.firebaseUser

    fun registrationWithEmailAndPassword(navigateToHomeScreen: () -> Unit) {
        viewModelScope.launch {
            if (emailState.isInvalid || passwordState.isInvalid) {
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
                authRepository.createUserWithEmail(emailState.value, passwordState.value)
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

            val isEmailVerified = checkEmailVerification()
            // Если пользователь не успел подтвердить электронную почту,
            // то удаляем аккаунт
            if (!isEmailVerified) {
                _isLoading.update { false }
                authRepository.deleteAccount()
                return@launch
            }

            // TODO добавить обработку в случае ошибки при создании пользователя
            userRepository.createUserData(firebaseUser?.uid)
            _isLoading.update { false }
            navigateToHomeScreen()
        }
    }

    fun getSelectedOptionDescription(selectedOption: AuthVariantsEnum): String =
        when (selectedOption) {
            AuthVariantsEnum.EMAIL -> AuthCardEnum.REGISTER_WITH_EMAIL.value
            AuthVariantsEnum.PHONE -> AuthCardEnum.REGISTER_WITH_PHONE.value
            AuthVariantsEnum.GOOGLE -> AuthCardEnum.REGISTER_WITH_GOOGLE.value
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
        viewModelScope.launch {
            authOptionsState.valueChanges.collect {
                emailState.reset()
                passwordState.reset()
                phoneState.reset()
                codeState.reset()
            }
        }
    }
}
