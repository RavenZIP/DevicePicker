package com.ravenzip.devicepicker.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.constants.enums.AuthCardEnum
import com.ravenzip.devicepicker.extensions.functions.defaultCardColors
import com.ravenzip.devicepicker.ui.components.BottomContainer
import com.ravenzip.devicepicker.ui.components.ScreenTitle
import com.ravenzip.devicepicker.viewmodels.auth.ForgotPasswordViewModel
import com.ravenzip.workshop.components.InfoCard
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.components.SinglenessTextField
import com.ravenzip.workshop.components.SnackBar
import com.ravenzip.workshop.components.Spinner
import com.ravenzip.workshop.data.icon.Icon

@Composable
fun ForgotPasswordScreen(
    forgotPasswordViewModel: ForgotPasswordViewModel = hiltViewModel<ForgotPasswordViewModel>()
) {
    val isLoadingState = forgotPasswordViewModel.isLoading.collectAsState().value
    val emailErrorsState = forgotPasswordViewModel.emailErrors.collectAsState().value
    val snackBarHostState = remember { forgotPasswordViewModel.snackBarHostState }

    val email = remember { mutableStateOf("") }

    val interactionSource = remember { MutableInteractionSource() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

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
            leadingIcon = Icon.ResourceIcon(R.drawable.i_email),
            error = emailErrorsState,
        )

        Spacer(modifier = Modifier.height(30.dp))
        InfoCard(
            icon = Icon.ResourceIcon(R.drawable.i_info),
            title = "Важно!",
            text = AuthCardEnum.FORGOT_PASS.value,
            colors = CardDefaults.defaultCardColors(),
        )
    }

    BottomContainer {
        Spacer(modifier = Modifier.height(20.dp))
        SimpleButton(text = "Продолжить") {
            forgotPasswordViewModel.resetPassword(email = email.value)
        }

        Spacer(modifier = Modifier.height(20.dp))
    }

    if (isLoadingState) {
        Spinner(text = "Отправка ссылки для сброса пароля...")
    }

    SnackBar(snackBarHostState = snackBarHostState)
}
