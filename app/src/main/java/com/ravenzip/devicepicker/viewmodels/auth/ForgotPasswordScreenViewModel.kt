package com.ravenzip.devicepicker.viewmodels.auth

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.extensions.functions.showError
import com.ravenzip.devicepicker.extensions.functions.showSuccess
import com.ravenzip.devicepicker.repositories.AuthRepository
import com.ravenzip.devicepicker.services.ValidationService
import com.ravenzip.workshop.data.Error
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ForgotPasswordScreenViewModel
@Inject
constructor(
    private val authRepository: AuthRepository,
    private val validationService: ValidationService,
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    private val _emailErrors = MutableStateFlow(Error())

    val isLoading = _isLoading.asStateFlow()
    val emailErrors = _emailErrors.asStateFlow()
    val snackBarHostState = SnackbarHostState()

    fun resetPassword(email: String) {
        viewModelScope.launch {
            val emailError = validationService.checkEmail(email)
            _emailErrors.update { emailError }

            if (emailError.value) {
                snackBarHostState.showError("Проверьте правильность заполнения поля")
                return@launch
            }

            _isLoading.update { true }

            val isReloadSuccess = authRepository.reloadUser()
            if (isReloadSuccess.value != true) {
                _isLoading.update { false }
                snackBarHostState.showError(isReloadSuccess.error?.message!!)
                return@launch
            }

            val resetResult = authRepository.sendPasswordResetEmail(email)
            _isLoading.update { false }

            if (resetResult.value == true) {
                snackBarHostState.showSuccess(
                    "Письмо со ссылкой для сброса было успешно отправлено на почту"
                )
            } else {
                snackBarHostState.showError(resetResult.error?.message!!)
            }
        }
    }
}
