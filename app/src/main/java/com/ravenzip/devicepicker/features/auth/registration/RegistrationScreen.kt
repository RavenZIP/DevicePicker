package com.ravenzip.devicepicker.features.auth.registration

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
import com.ravenzip.devicepicker.common.enums.AuthTypeEnum
import com.ravenzip.devicepicker.common.utils.extension.defaultCardColors
import com.ravenzip.devicepicker.features.auth.common.AuthFields
import com.ravenzip.devicepicker.features.auth.common.AuthOptions
import com.ravenzip.workshop.components.InfoCard
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.components.SnackBar
import com.ravenzip.workshop.components.Spinner
import com.ravenzip.workshop.data.icon.IconData

@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel = hiltViewModel(),
    navigateToHomeScreen: () -> Unit,
) {
    val isLoadingState = viewModel.isLoading.collectAsStateWithLifecycle().value
    val spinnerTextState = viewModel.spinnerText.collectAsStateWithLifecycle().value

    val snackBarHostState = remember { viewModel.snackBarHostState }

    val interactionSource = remember { MutableInteractionSource() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

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
        AuthFields(
            selectedOption = viewModel.form.controls.authType.value,
            emailControl = viewModel.form.controls.email,
            passwordControl = viewModel.form.controls.password,
            phoneControl = viewModel.form.controls.phone,
            codeControl = viewModel.form.controls.code,
        )

        Spacer(modifier = Modifier.height(30.dp))
        AuthOptions(
            control = viewModel.form.controls.authType,
            title = "Выбор варианта регистрации",
        )

        Spacer(modifier = Modifier.height(20.dp))
        InfoCard(
            icon = IconData.ResourceIcon(R.drawable.i_info),
            title = "Важно!",
            text = viewModel.getSelectedOptionDescription(viewModel.form.controls.authType.value),
            colors = CardDefaults.defaultCardColors(),
        )

        Spacer(modifier = Modifier.padding(bottom = 120.dp))
    }

    BottomContainer {
        SimpleButton(text = "Продолжить") {
            when (viewModel.form.controls.authType.value) {
                AuthTypeEnum.EMAIL -> {
                    viewModel.registrationWithEmailAndPassword(
                        navigateToHomeScreen = navigateToHomeScreen
                    )
                }

                AuthTypeEnum.PHONE -> {}
            }
        }
    }

    if (isLoadingState) {
        Spinner(text = spinnerTextState)
    }

    SnackBar(snackBarHostState = snackBarHostState)
}
