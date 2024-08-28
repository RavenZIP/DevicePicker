package com.ravenzip.devicepicker.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.constants.enums.AuthCardEnum
import com.ravenzip.devicepicker.constants.enums.AuthVariantsEnum
import com.ravenzip.devicepicker.extensions.functions.defaultCardColors
import com.ravenzip.devicepicker.services.ValidationService
import com.ravenzip.devicepicker.services.showError
import com.ravenzip.devicepicker.services.showWarning
import com.ravenzip.devicepicker.ui.components.BottomContainer
import com.ravenzip.devicepicker.ui.components.ScreenTitle
import com.ravenzip.devicepicker.ui.screens.auth.common.LoginAndRegistrationFields
import com.ravenzip.devicepicker.ui.screens.auth.common.LoginAndRegistrationOptions
import com.ravenzip.devicepicker.ui.screens.auth.common.generateAuthVariants
import com.ravenzip.devicepicker.ui.screens.auth.common.getSelectedVariant
import com.ravenzip.devicepicker.viewmodels.UserViewModel
import com.ravenzip.workshop.components.InfoCard
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.components.SnackBar
import com.ravenzip.workshop.components.Spinner
import com.ravenzip.workshop.data.Error
import com.ravenzip.workshop.data.icon.IconConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RegistrationScreen(userViewModel: UserViewModel, navigateToHomeScreen: () -> Unit) {
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
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        ScreenTitle(text = "Регистрация")

        Spacer(modifier = Modifier.height(30.dp))
        LoginAndRegistrationFields(
            selectedVariant = selectedRegisterVariant,
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
        LoginAndRegistrationOptions(
            options = registerVariants,
            title = "Выбор варианта регистрации",
        )

        Spacer(modifier = Modifier.height(20.dp))
        InfoCard(
            icon = ImageVector.vectorResource(R.drawable.i_info),
            iconConfig = IconConfig.PrimarySmall,
            title = "Важно!",
            text = getCardText(selectedRegisterVariant),
            colors = CardDefaults.defaultCardColors(),
        )

        Spacer(modifier = Modifier.padding(bottom = 120.dp))
    }

    BottomContainer {
        Spacer(modifier = Modifier.height(20.dp))
        SimpleButton(text = "Продолжить") {
            scope.launch(Dispatchers.Main) {
                when (selectedRegisterVariant()) {
                    AuthVariantsEnum.EMAIL -> {
                        emailError.value = validationService.checkEmail(email.value)
                        passwordError.value = validationService.checkPassword(password.value)

                        if (emailError.value.value || passwordError.value.value) {
                            snackBarHostState.showError("Проверьте правильность заполнения полей")
                            return@launch
                        }
                        isLoading.value = true

                        val isReloadSuccess = userViewModel.reloadUser()
                        if (isReloadSuccess.value != true) {
                            isLoading.value = false
                            snackBarHostState.showError(isReloadSuccess.error?.message!!)
                            return@launch
                        }

                        spinnerText.value = "Регистрация..."
                        val authResult =
                            userViewModel.createUserWithEmail(email.value, password.value)

                        if (authResult.value == null) {
                            isLoading.value = false
                            snackBarHostState.showError(authResult.error?.message!!)
                            return@launch
                        }

                        spinnerText.value = "Отправка письма с подтверждением..."
                        val messageResult = userViewModel.sendEmailVerification()
                        if (messageResult.value != true) {
                            isLoading.value = false
                            snackBarHostState.showWarning(messageResult.error?.message!!)
                            userViewModel.deleteAccount() // TODO проверять падение запроса
                            return@launch
                        }

                        spinnerText.value = "Ожидание подтверждения электронной почты..."
                        val timer =
                            checkEmailVerificationEverySecondAndGetTimer(
                                isEmailVerified = { userViewModel.isEmailVerified() }
                            )
                        // Если пользователь не успел подтвердить электронную почту,
                        // то удаляем аккаунт
                        if (timer == 0) {
                            isLoading.value = false
                            userViewModel.deleteAccount()
                            return@launch
                        }

                        // TODO добавить обработку в случае ошибки при создании пользователя
                        userViewModel.createUserData()
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
        Spinner(text = spinnerText.value)
    }

    SnackBar(snackBarHostState = snackBarHostState)
}

private fun getCardText(selectedRegisterVariant: () -> AuthVariantsEnum): String =
    when (selectedRegisterVariant()) {
        AuthVariantsEnum.EMAIL -> AuthCardEnum.REGISTER_WITH_EMAIL.value
        AuthVariantsEnum.PHONE -> AuthCardEnum.REGISTER_WITH_PHONE.value
        AuthVariantsEnum.GOOGLE -> AuthCardEnum.REGISTER_WITH_GOOGLE.value
    }

private suspend fun checkEmailVerificationEverySecondAndGetTimer(
    isEmailVerified: suspend () -> Boolean
): Int {
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
