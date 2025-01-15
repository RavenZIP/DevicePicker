package com.ravenzip.devicepicker.viewmodels.auth

import android.util.Patterns.PHONE
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.constants.enums.AuthVariantsEnum
import com.ravenzip.devicepicker.extensions.functions.showError
import com.ravenzip.devicepicker.model.result.Result
import com.ravenzip.devicepicker.repositories.AuthRepository
import com.ravenzip.workshop.forms.Validators
import com.ravenzip.workshop.forms.state.FormState
import com.ravenzip.workshop.forms.state.special.TextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)

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
    val snackBarHostState = SnackbarHostState()

    fun logInWithEmailAndPassword(navigateToHomeScreen: () -> Unit) {
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

            val authResult =
                authRepository.logInUserWithEmail(emailState.value, passwordState.value)
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
            authOptionsState.valueChanges.collect {
                emailState.reset()
                passwordState.reset()
                phoneState.reset()
                codeState.reset()
            }
        }
    }
}
