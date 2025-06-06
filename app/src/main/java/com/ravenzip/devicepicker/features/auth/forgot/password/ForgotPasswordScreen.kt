package com.ravenzip.devicepicker.features.auth.forgot.password

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.common.components.BottomContainer
import com.ravenzip.devicepicker.common.components.ScreenTitle
import com.ravenzip.devicepicker.common.enums.AuthCardEnum
import com.ravenzip.devicepicker.common.utils.extension.defaultCardColors
import com.ravenzip.workshop.components.InfoCard
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.components.SinglenessOutlinedTextField
import com.ravenzip.workshop.components.SnackBar
import com.ravenzip.workshop.components.Spinner
import com.ravenzip.workshop.data.icon.IconData

@Composable
fun ForgotPasswordScreen(viewModel: ForgotPasswordViewModel = hiltViewModel()) {
    val isLoadingState = viewModel.isLoading.collectAsStateWithLifecycle().value
    val snackBarHostState = remember { viewModel.snackBarHostState }

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
        SinglenessOutlinedTextField(
            control = viewModel.emailControl,
            label = "Электронная почта",
            leadingIcon = IconData.ResourceIcon(R.drawable.i_email),
        )

        Spacer(modifier = Modifier.height(30.dp))
        InfoCard(
            icon = IconData.ResourceIcon(R.drawable.i_info),
            title = "Важно!",
            text = AuthCardEnum.FORGOT_PASS.value,
            colors = CardDefaults.defaultCardColors(),
        )
    }

    BottomContainer { SimpleButton(text = "Продолжить") { viewModel.resetPassword() } }

    if (isLoadingState) {
        Spinner(text = "Отправка ссылки для сброса пароля...")
    }

    SnackBar(snackBarHostState = snackBarHostState)
}
