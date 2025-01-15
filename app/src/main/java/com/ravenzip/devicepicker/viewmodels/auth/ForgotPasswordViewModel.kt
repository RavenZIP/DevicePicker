package com.ravenzip.devicepicker.viewmodels.auth

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.extensions.functions.showError
import com.ravenzip.devicepicker.extensions.functions.showSuccess
import com.ravenzip.devicepicker.model.result.Result
import com.ravenzip.devicepicker.repositories.AuthRepository
import com.ravenzip.workshop.forms.Validators
import com.ravenzip.workshop.forms.state.special.TextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {
    private val _isLoading = MutableStateFlow(false)

    val emailState =
        TextFieldState(
            initialValue = "",
            validators =
                listOf(
                    { value -> Validators.required(value) },
                    { value -> Validators.email(value) },
                ),
        )

    val isLoading = _isLoading.asStateFlow()
    val snackBarHostState = SnackbarHostState()

    fun resetPassword() {
        viewModelScope.launch {
            if (emailState.isInvalid) {
                snackBarHostState.showError("Проверьте правильность заполнения поля")
                return@launch
            }

            _isLoading.update { true }

            val reloadResult = authRepository.reloadUser()

            if (reloadResult is Result.Error) {
                _isLoading.update { false }
                snackBarHostState.showError(reloadResult.message)
                return@launch
            }

            val resetResult = authRepository.sendPasswordResetEmail(emailState.value)
            _isLoading.update { false }

            if (resetResult is Result.Error) {
                snackBarHostState.showError(resetResult.message)
                return@launch
            }

            snackBarHostState.showSuccess(
                "Письмо со ссылкой для сброса было успешно отправлено на почту"
            )
        }
    }
}
