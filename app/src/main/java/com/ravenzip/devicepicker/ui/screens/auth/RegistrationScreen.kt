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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.constants.enums.AuthVariantsEnum
import com.ravenzip.devicepicker.extensions.functions.defaultCardColors
import com.ravenzip.devicepicker.ui.components.BottomContainer
import com.ravenzip.devicepicker.ui.components.ScreenTitle
import com.ravenzip.devicepicker.ui.screens.auth.common.AuthFields
import com.ravenzip.devicepicker.ui.screens.auth.common.AuthOptions
import com.ravenzip.devicepicker.viewmodels.auth.RegistrationViewModel
import com.ravenzip.workshop.components.InfoCard
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.components.SnackBar
import com.ravenzip.workshop.components.Spinner
import com.ravenzip.workshop.data.icon.Icon

@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel = hiltViewModel(),
    navigateToHomeScreen: () -> Unit,
) {
    val isLoadingState = viewModel.isLoading.collectAsState().value
    val spinnerTextState = viewModel.spinnerText.collectAsState().value

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
            selectedOption = viewModel.authOptionsState.value,
            emailState = viewModel.emailState,
            passwordState = viewModel.passwordState,
            phoneState = viewModel.phoneState,
            codeState = viewModel.codeState,
        )

        Spacer(modifier = Modifier.height(30.dp))
        AuthOptions(formState = viewModel.authOptionsState, title = "Выбор варианта регистрации")

        Spacer(modifier = Modifier.height(20.dp))
        InfoCard(
            icon = Icon.ResourceIcon(R.drawable.i_info),
            title = "Важно!",
            text = viewModel.getSelectedOptionDescription(viewModel.authOptionsState.value),
            colors = CardDefaults.defaultCardColors(),
        )

        Spacer(modifier = Modifier.padding(bottom = 120.dp))
    }

    BottomContainer {
        Spacer(modifier = Modifier.height(20.dp))
        SimpleButton(text = "Продолжить") {
            when (viewModel.authOptionsState.value) {
                AuthVariantsEnum.EMAIL -> {
                    viewModel.registrationWithEmailAndPassword(
                        navigateToHomeScreen = navigateToHomeScreen
                    )
                }

                AuthVariantsEnum.PHONE -> {}
                AuthVariantsEnum.GOOGLE -> {}
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }

    if (isLoadingState) {
        Spinner(text = spinnerTextState)
    }

    SnackBar(snackBarHostState = snackBarHostState)
}
