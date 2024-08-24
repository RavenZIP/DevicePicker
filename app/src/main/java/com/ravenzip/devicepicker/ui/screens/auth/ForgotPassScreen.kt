package com.ravenzip.devicepicker.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import com.ravenzip.devicepicker.extensions.functions.defaultCardColors
import com.ravenzip.devicepicker.model.result.Result
import com.ravenzip.devicepicker.services.ValidationService
import com.ravenzip.devicepicker.services.showError
import com.ravenzip.devicepicker.services.showSuccess
import com.ravenzip.devicepicker.ui.components.BottomContainer
import com.ravenzip.devicepicker.ui.components.ScreenTitle
import com.ravenzip.workshop.components.InfoCard
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.components.SinglenessTextField
import com.ravenzip.workshop.components.SnackBar
import com.ravenzip.workshop.components.Spinner
import com.ravenzip.workshop.data.Error
import com.ravenzip.workshop.data.icon.IconConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ForgotPasswordScreen(
    reloadUser: suspend () -> Result<Boolean>,
    sendPasswordResetEmail: suspend (email: String) -> Result<Boolean>,
) {
    val email = remember { mutableStateOf("") }

    val validationService = ValidationService()
    val emailError = remember { mutableStateOf(Error()) }

    val interactionSource = remember { MutableInteractionSource() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val isLoading = remember { mutableStateOf(false) }
    val spinnerText = remember { mutableStateOf("Отправка ссылки для сброса пароля...") }

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
        ScreenTitle(text = "Сброс и восстановление")

        Spacer(modifier = Modifier.height(30.dp))
        SinglenessTextField(
            text = email,
            label = "Электронная почта",
            leadingIcon = ImageVector.vectorResource(R.drawable.i_email),
            error = emailError.value,
        )

        Spacer(modifier = Modifier.height(30.dp))
        InfoCard(
            icon = ImageVector.vectorResource(R.drawable.i_info),
            iconConfig = IconConfig.PrimarySmall,
            title = "Важно!",
            text = AuthCardEnum.FORGOT_PASS.value,
            colors = CardDefaults.defaultCardColors(),
        )
    }

    BottomContainer {
        Spacer(modifier = Modifier.height(20.dp))
        SimpleButton(text = "Продолжить") {
            scope.launch(Dispatchers.Main) {
                emailError.value = validationService.checkEmail(email.value)

                if (emailError.value.value) {
                    snackBarHostState.showError("Проверьте правильность заполнения поля")
                    return@launch
                }
                isLoading.value = true

                val isReloadSuccess = reloadUser()
                if (isReloadSuccess.value != true) {
                    isLoading.value = false
                    snackBarHostState.showError(isReloadSuccess.error!!)
                    return@launch
                }

                spinnerText.value = "Отправка ссылки для сброса пароля..."
                val resetResult = sendPasswordResetEmail(email.value)
                isLoading.value = false

                if (resetResult.value == true) {
                    snackBarHostState.showSuccess(
                        "Письмо со ссылкой для сброса было успешно отправлено на почту"
                    )
                } else {
                    snackBarHostState.showError(resetResult.error!!)
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
