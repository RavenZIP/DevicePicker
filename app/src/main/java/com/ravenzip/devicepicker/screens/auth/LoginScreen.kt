package com.ravenzip.devicepicker.screens.auth

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
import com.ravenzip.devicepicker.components.AuthVariants
import com.ravenzip.devicepicker.components.BottomContainer
import com.ravenzip.devicepicker.components.GetFields
import com.ravenzip.devicepicker.components.ScreenTitle
import com.ravenzip.devicepicker.components.generateAuthVariants
import com.ravenzip.devicepicker.components.getSelectedVariant
import com.ravenzip.devicepicker.enums.AuthVariantsEnum
import com.ravenzip.devicepicker.extensions.functions.inverseMixColors
import com.ravenzip.devicepicker.services.ValidationService
import com.ravenzip.devicepicker.services.showError
import com.ravenzip.devicepicker.viewmodels.UserViewModel
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.components.SnackBar
import com.ravenzip.workshop.components.Spinner
import com.ravenzip.workshop.data.Error
import com.ravenzip.workshop.data.TextParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    userViewModel: UserViewModel,
    navigateToHomeScreen: () -> Unit,
    navigateToForgotPassScreen: () -> Unit
) {
    val emailOrPhone = remember { mutableStateOf("") }
    val passwordOrCode = remember { mutableStateOf("") }

    val validationService = ValidationService()
    val emailOrPhoneError = remember { mutableStateOf(Error()) }
    val passwordOrCodeError = remember { mutableStateOf(Error()) }

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
                interactionSource = interactionSource, indication = null) {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                },
        horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(40.dp))
            ScreenTitle(text = "Войти в аккаунт")

            Spacer(modifier = Modifier.height(30.dp))
            GetFields(
                selectedVariant = selectedLoginVariant,
                fields = listOf(emailOrPhone, passwordOrCode),
                validation = arrayOf(emailOrPhoneError.value, passwordOrCodeError.value))

            Spacer(modifier = Modifier.height(30.dp))
            AuthVariants(authVariants = loginVariants, title = "Выбор варианта входа")
        }

    BottomContainer {
        Spacer(modifier = Modifier.height(20.dp))
        SimpleButton(
            text = TextParameters(value = "Продолжить", size = 16),
        ) {
            scope.launch(Dispatchers.Main) {
                when (selectedLoginVariant()) {
                    AuthVariantsEnum.EMAIL -> {
                        emailOrPhoneError.value = validationService.checkEmail(emailOrPhone.value)
                        passwordOrCodeError.value =
                            validationService.checkPassword(passwordOrCode.value)

                        if (emailOrPhoneError.value.value || passwordOrCodeError.value.value) {
                            snackBarHostState.showError("Проверьте правильность заполнения полей")
                            return@launch
                        }
                        isLoading.value = true

                        val isReloadSuccess = userViewModel.reloadUser()
                        if (isReloadSuccess.value != true) {
                            isLoading.value = false
                            snackBarHostState.showError(isReloadSuccess.error!!)
                            return@launch
                        }

                        spinnerText.value = "Вход в аккаунт..."
                        val authResult =
                            userViewModel.logInUserWithEmail(
                                emailOrPhone.value, passwordOrCode.value)

                        if (authResult.value == null) {
                            isLoading.value = false
                            snackBarHostState.showError(authResult.error!!)
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
        SimpleButton(
            text = TextParameters(value = "Забыли пароль?", size = 16),
            colors = ButtonDefaults.inverseMixColors()) {
                navigateToForgotPassScreen()
            }

        Spacer(modifier = Modifier.height(20.dp))
    }

    if (isLoading.value) {
        Spinner(text = TextParameters(value = spinnerText.value, size = 16))
    }

    SnackBar(snackBarHostState = snackBarHostState)
}
