package com.ravenzip.devicepicker.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravenzip.devicepicker.components.AuthVariants
import com.ravenzip.devicepicker.components.BottomContainer
import com.ravenzip.devicepicker.components.GetFields
import com.ravenzip.devicepicker.components.generateAuthVariants
import com.ravenzip.devicepicker.components.getSelectedVariant
import com.ravenzip.devicepicker.enums.AuthVariantsEnum
import com.ravenzip.devicepicker.extensions.functions.getInverseMixColors
import com.ravenzip.devicepicker.services.firebase.logInUserWithEmail
import com.ravenzip.devicepicker.services.firebase.reloadUser
import com.ravenzip.devicepicker.services.isEmailValid
import com.ravenzip.devicepicker.services.isPasswordValid
import com.ravenzip.devicepicker.services.showError
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.components.SnackBar
import com.ravenzip.workshop.components.Spinner
import com.ravenzip.workshop.data.TextParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navigateToHomeScreen: () -> Unit, navigateToForgotPassScreen: () -> Unit) {
    val emailOrPhone = remember { mutableStateOf("") }
    val passwordOrCode = remember { mutableStateOf("") }
    val isEmailOrPhoneValid = remember { mutableStateOf(true) }
    val isPasswordOrCodeValid = remember { mutableStateOf(true) }
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
                indication = null
            ) {
                focusManager.clearFocus()
                keyboardController?.hide()
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "Войти в аккаунт",
            modifier = Modifier.align(Alignment.Start).padding(start = 20.dp, end = 20.dp),
            fontSize = 25.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.sp
        )

        Spacer(modifier = Modifier.height(30.dp))
        GetFields(
            selectedVariant = selectedLoginVariant,
            fields = listOf(emailOrPhone, passwordOrCode),
            validation = listOf(isEmailOrPhoneValid.value, isPasswordOrCodeValid.value)
        )

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
                        isEmailOrPhoneValid.value = isEmailValid(emailOrPhone.value)
                        isPasswordOrCodeValid.value = isPasswordValid(passwordOrCode.value)

                        if (!isEmailOrPhoneValid.value || !isPasswordOrCodeValid.value) {
                            snackBarHostState.showError("Проверьте правильность заполнения полей")
                            return@launch
                        }
                        isLoading.value = true

                        val isReloadSuccess = reloadUser()
                        if (isReloadSuccess.value != true) {
                            isLoading.value = false
                            snackBarHostState.showError(isReloadSuccess.error!!)
                            return@launch
                        }

                        spinnerText.value = "Вход в аккаунт..."
                        val authResult =
                            logInUserWithEmail(emailOrPhone.value, passwordOrCode.value)

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
            colors = getInverseMixColors()
        ) {
            navigateToForgotPassScreen()
        }

        Spacer(modifier = Modifier.height(20.dp))
    }

    if (isLoading.value) {
        Spinner(text = TextParameters(value = spinnerText.value, size = 16))
    }

    SnackBar(snackBarHostState = snackBarHostState)
}
