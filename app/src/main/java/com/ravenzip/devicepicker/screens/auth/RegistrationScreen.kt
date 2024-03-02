package com.ravenzip.devicepicker.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravenzip.devicepicker.enums.AuthCardEnum
import com.ravenzip.devicepicker.enums.AuthVariantsEnum
import com.ravenzip.devicepicker.services.createUserWithEmail
import com.ravenzip.devicepicker.services.deleteAccount
import com.ravenzip.devicepicker.services.isEmailValid
import com.ravenzip.devicepicker.services.isEmailVerified
import com.ravenzip.devicepicker.services.isPasswordValid
import com.ravenzip.devicepicker.services.reloadUser
import com.ravenzip.devicepicker.services.sendEmailVerification
import com.ravenzip.devicepicker.services.showError
import com.ravenzip.devicepicker.services.showWarning
import com.ravenzip.devicepicker.ui.components.BottomContainer
import com.ravenzip.devicepicker.ui.components.auth.AuthVariants
import com.ravenzip.devicepicker.ui.components.auth.GetFields
import com.ravenzip.devicepicker.ui.components.auth.generateAuthVariants
import com.ravenzip.devicepicker.ui.components.auth.getSelectedVariant
import com.ravenzip.devicepicker.ui.components.default.getDefaultColors
import com.ravenzip.workshop.components.InfoCard
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.components.SnackBar
import com.ravenzip.workshop.components.Spinner
import com.ravenzip.workshop.data.IconParameters
import com.ravenzip.workshop.data.TextParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RegistrationScreen(navigateToHomeScreen: () -> Unit) {
    val emailOrPhone = remember { mutableStateOf("") }
    val passwordOrCode = remember { mutableStateOf("") }
    val isEmailOrPhoneValid = remember { mutableStateOf(true) }
    val isPasswordOrCodeValid = remember { mutableStateOf(true) }
    val interactionSource = remember { MutableInteractionSource() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val registerVariants = remember { generateAuthVariants() }
    val selectedRegisterVariant = remember { { getSelectedVariant(registerVariants) } }
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val isLoading = remember { mutableStateOf(false) }
    val spinnerText = remember { mutableStateOf("Регистрация...") }

    Column(
        modifier =
            Modifier.fillMaxSize()
                .clickable(interactionSource = interactionSource, indication = null) {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "Регистрация",
            modifier = Modifier.align(Alignment.Start).padding(start = 20.dp, end = 20.dp),
            fontSize = 25.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.sp
        )

        Spacer(modifier = Modifier.height(30.dp))
        GetFields(
            selectedVariant = selectedRegisterVariant,
            fields = listOf(emailOrPhone, passwordOrCode),
            validation = listOf(isEmailOrPhoneValid.value, isPasswordOrCodeValid.value)
        )

        Spacer(modifier = Modifier.height(30.dp))
        AuthVariants(authVariants = registerVariants, title = "Выбор варианта регистрации")

        Spacer(modifier = Modifier.height(20.dp))
        InfoCard(
            icon =
                IconParameters(
                    value = Icons.Outlined.Info,
                    color = MaterialTheme.colorScheme.primary
                ),
            title = TextParameters(value = "Важно!", size = 20),
            text = TextParameters(value = getCardText(selectedRegisterVariant), size = 14),
            colors = getDefaultColors()
        )

        Spacer(modifier = Modifier.padding(bottom = 120.dp))
    }

    BottomContainer {
        Spacer(modifier = Modifier.height(20.dp))
        SimpleButton(
            text = TextParameters(value = "Продолжить", size = 16),
            textAlign = TextAlign.Center
        ) {
            scope.launch(Dispatchers.Main) {
                when (selectedRegisterVariant()) {
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

                        spinnerText.value = "Регистрация..."
                        val authResult =
                            createUserWithEmail(emailOrPhone.value, passwordOrCode.value)

                        if (authResult.value == null) {
                            isLoading.value = false
                            snackBarHostState.showError(authResult.error!!)
                            return@launch
                        }

                        spinnerText.value = "Отправка письма с подтверждением..."
                        val messageResult = sendEmailVerification()
                        if (messageResult.value != true) {
                            isLoading.value = false
                            snackBarHostState.showWarning(messageResult.error!!)
                            deleteAccount()
                            return@launch
                        }

                        spinnerText.value = "Ожидание подтверждения электронной почты..."
                        val timer = checkEmailVerificationEverySecondAndGetTimer()
                        // Если пользователь не успел подтвердить электронную почту,
                        // то удаляем аккаунт
                        if (timer == 0) {
                            isLoading.value = false
                            deleteAccount()
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
    }

    if (isLoading.value) {
        Spinner(text = TextParameters(value = spinnerText.value, size = 16))
    }

    SnackBar(snackBarHostState = snackBarHostState)
}

private fun getCardText(selectedRegisterVariant: () -> AuthVariantsEnum): String {
    return when (selectedRegisterVariant()) {
        AuthVariantsEnum.EMAIL -> AuthCardEnum.REGISTER_WITH_EMAIL.value
        AuthVariantsEnum.PHONE -> AuthCardEnum.REGISTER_WITH_PHONE.value
        AuthVariantsEnum.GOOGLE -> AuthCardEnum.REGISTER_WITH_GOOGLE.value
    }
}

private suspend fun checkEmailVerificationEverySecondAndGetTimer(): Int {
    var timer = 25 // Время, за которое необходимо зарегистрироваться пользователю
    while (timer > 0) {
        if (isEmailVerified()) {
            timer = -1
        } else {
            timer -= 1
            delay(1000)
        }
    }
    return timer
}
