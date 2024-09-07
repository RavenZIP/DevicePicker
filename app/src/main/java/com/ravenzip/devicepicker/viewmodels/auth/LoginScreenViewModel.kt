package com.ravenzip.devicepicker.viewmodels.auth

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.constants.enums.AuthVariantsEnum
import com.ravenzip.devicepicker.extensions.functions.showError
import com.ravenzip.devicepicker.repositories.AuthRepository
import com.ravenzip.devicepicker.services.AuthService
import com.ravenzip.devicepicker.services.ValidationService
import com.ravenzip.devicepicker.state.AuthErrorState
import com.ravenzip.workshop.data.selection.SelectableItemConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LoginScreenViewModel
@Inject
constructor(
    private val authRepository: AuthRepository,
    private val authService: AuthService,
    private val validationService: ValidationService,
) : ViewModel() {
    private val _authOptions = MutableStateFlow(authService.createAuthOptions())
    private val _isLoading = MutableStateFlow(false)
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
    val fieldErrors = _fieldErrors.asStateFlow()
    val snackBarHostState = SnackbarHostState()

    fun selectOption(item: SelectableItemConfig) {
        authService.selectOption(item, _authOptions.value) { updatedOptions ->
            _authOptions.update { updatedOptions }
        }
    }

    fun logInWithEmailAndPassword(
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

            val reloadResult = authRepository.reloadUser()
            if (reloadResult.value != true) {
                _isLoading.update { false }
                snackBarHostState.showError(reloadResult.error?.message!!)
                return@launch
            }

            val authResult = authRepository.logInUserWithEmail(email, password)
            if (authResult.value == null) {
                _isLoading.update { false }
                snackBarHostState.showError(authResult.error?.message!!)
                return@launch
            }

            _isLoading.update { false }
            navigateToHomeScreen()
        }
    }
}
