package com.ravenzip.devicepicker.viewmodels.auth

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.ravenzip.devicepicker.constants.enums.AuthCardEnum
import com.ravenzip.devicepicker.constants.enums.AuthVariantsEnum
import com.ravenzip.devicepicker.extensions.functions.showError
import com.ravenzip.devicepicker.extensions.functions.showWarning
import com.ravenzip.devicepicker.repositories.AuthRepository
import com.ravenzip.devicepicker.repositories.UserRepository
import com.ravenzip.devicepicker.services.AuthService
import com.ravenzip.devicepicker.services.ValidationService
import com.ravenzip.devicepicker.state.AuthErrorState
import com.ravenzip.workshop.data.selection.SelectableItemConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class RegistrationViewModel
@Inject
constructor(
    private val authRepository: AuthRepository,
    private val authService: AuthService,
    private val userRepository: UserRepository,
    private val validationService: ValidationService,
) : ViewModel() {
    private val _authOptions = MutableStateFlow(authService.createAuthOptions())
    private val _isLoading = MutableStateFlow(false)
    private val _spinnerText = MutableStateFlow("Регистрация...")
    private val _fieldErrors = MutableStateFlow(AuthErrorState.default())

    val selectedOption =
        _authOptions
            .map { options -> authService.calculateSelectedOption(options) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = AuthVariantsEnum.EMAIL,
            )
    val authOptions = _authOptions.asStateFlow()
    val isLoading = _isLoading.asStateFlow()
    val spinnerText = _spinnerText.asStateFlow()
    val fieldErrors = _fieldErrors.asStateFlow()
    val snackBarHostState = SnackbarHostState()

    /**
     * Текущий пользователь firebase
     *
     * @return [FirebaseUser] или null
     */
    private val firebaseUser: FirebaseUser?
        get() = authRepository.firebaseUser

    fun selectOption(item: SelectableItemConfig) {
        authService.selectOption(item, _authOptions.value) { updatedOptions ->
            _authOptions.update { updatedOptions }
        }
    }

    fun registrationWithEmailAndPassword(
        email: String,
        password: String,
        navigateToHomeScreen: () -> Unit,
    ) {
        viewModelScope.launch {
            val emailError = validationService.checkEmail(email)
            val passwordError = validationService.checkPassword(password)
            _fieldErrors.update {
                _fieldErrors.value.copy(email = emailError, password = passwordError)
            }

            if (emailError.value || passwordError.value) {
                snackBarHostState.showError("Проверьте правильность заполнения полей")
                return@launch
            }

            _isLoading.update { true }

            val isReloadSuccess = authRepository.reloadUser()
            if (isReloadSuccess.value != true) {
                _isLoading.update { false }
                snackBarHostState.showError(isReloadSuccess.error?.message!!)
                return@launch
            }

            _spinnerText.update { "Регистрация..." }

            val authResult = authRepository.createUserWithEmail(email, password)
            if (authResult.value == null) {
                _isLoading.update { false }
                snackBarHostState.showError(authResult.error?.message!!)
                return@launch
            }

            _spinnerText.update { "Отправка письма с подтверждением..." }

            val messageResult = authRepository.sendEmailVerification()
            if (messageResult.value != true) {
                _isLoading.update { false }
                snackBarHostState.showWarning(messageResult.error?.message!!)
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
}
