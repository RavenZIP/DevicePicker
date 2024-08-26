package com.ravenzip.devicepicker.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.AuthResult
import com.ravenzip.devicepicker.constants.enums.AuthVariantsEnum
import com.ravenzip.devicepicker.extensions.functions.inverseMixColors
import com.ravenzip.devicepicker.model.result.Result
import com.ravenzip.devicepicker.services.ValidationService
import com.ravenzip.devicepicker.services.showError
import com.ravenzip.devicepicker.ui.components.BottomContainer
import com.ravenzip.devicepicker.ui.components.ScreenTitle
import com.ravenzip.devicepicker.ui.screens.auth.common.LoginAndRegistrationFields
import com.ravenzip.devicepicker.ui.screens.auth.common.LoginAndRegistrationOptions
import com.ravenzip.devicepicker.ui.screens.auth.common.generateAuthVariants
import com.ravenzip.devicepicker.ui.screens.auth.common.getSelectedVariant
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.components.SnackBar
import com.ravenzip.workshop.components.Spinner
import com.ravenzip.workshop.data.Error
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    reloadUser: suspend () -> Result<Boolean>,
    logInUserWithEmail: suspend (email: String, password: String) -> Result<AuthResult?>,
    navigateToHomeScreen: () -> Unit,
    navigateToForgotPassScreen: () -> Unit,
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val phone = remember { mutableStateOf("") }
    val code = remember { mutableStateOf("") }

    val validationService = remember { ValidationService() }
    val emailError = remember { mutableStateOf(Error()) }
    val passwordError = remember { mutableStateOf(Error()) }
    val phoneError = remember { mutableStateOf(Error()) }
    val codeError = remember { mutableStateOf(Error()) }

    val interactionSource = remember { MutableInteractionSource() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val loginVariants = remember { generateAuthVariants() }
    val selectedLoginVariant = remember { { getSelectedVariant(loginVariants) } }

    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val isLoading = remember { mutableStateOf(false) }
    val spinnerText = remember { mutableStateOf("Вход в аккаунт...") }

    Column(
        modifier =
            Modifier.fillMaxSize().clickable(
                interactionSource = interactionSource,
                indication = null,
            ) {
                focusManager.clearFocus()
                keyboardController?.hide()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        ScreenTitle(text = "Войти в аккаунт")

        Spacer(modifier = Modifier.height(30.dp))
        LoginAndRegistrationFields(
            selectedVariant = selectedLoginVariant,
            email = email,
            emailError = emailError,
            password = password,
            passwordError = passwordError,
            phone = phone,
            phoneError = phoneError,
            code = code,
            codeError = codeError,
        )

        Spacer(modifier = Modifier.height(30.dp))
        LoginAndRegistrationOptions(options = loginVariants, title = "Выбор варианта входа")
    }

    BottomContainer {
        Spacer(modifier = Modifier.height(20.dp))
        SimpleButton(text = "Продолжить") {
            scope.launch(Dispatchers.Main) {
                when (selectedLoginVariant()) {
                    AuthVariantsEnum.EMAIL -> {
                        emailError.value = validationService.checkEmail(email.value)
                        passwordError.value = validationService.checkPassword(password.value)

                        if (emailError.value.value || passwordError.value.value) {
                            snackBarHostState.showError("Проверьте правильность заполнения полей")
                            return@launch
                        }
                        isLoading.value = true

                        val isReloadSuccess = reloadUser()
                        if (isReloadSuccess.value != true) {
                            isLoading.value = false
                            snackBarHostState.showError(isReloadSuccess.error?.message!!)
                            return@launch
                        }

                        spinnerText.value = "Вход в аккаунт..."
                        val authResult = logInUserWithEmail(email.value, password.value)

                        if (authResult.value == null) {
                            isLoading.value = false
                            snackBarHostState.showError(authResult.error?.message!!)
                            return@launch
                        }

                        isLoading.value = false
                        navigateToHomeScreen()
                    }
                    AuthVariantsEnum.PHONE -> {}
                    AuthVariantsEnum.GOOGLE -> {}
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        SimpleButton(text = "Забыли пароль?", colors = ButtonDefaults.inverseMixColors()) {
            navigateToForgotPassScreen()
        }

        Spacer(modifier = Modifier.height(20.dp))
    }

    if (isLoading.value) {
        Spinner(text = spinnerText.value)
    }

    SnackBar(snackBarHostState = snackBarHostState)
}
