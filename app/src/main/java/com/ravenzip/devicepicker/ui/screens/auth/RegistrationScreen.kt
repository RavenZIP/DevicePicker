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
import androidx.compose.runtime.mutableStateOf
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
    registrationViewModel: RegistrationViewModel = hiltViewModel<RegistrationViewModel>(),
    navigateToHomeScreen: () -> Unit,
) {
    val authOptionsState = registrationViewModel.authOptions.collectAsState().value
    val selectedOptionState = registrationViewModel.selectedOption.collectAsState().value
    val fieldErrorsState = registrationViewModel.fieldErrors.collectAsState().value
    val isLoadingState = registrationViewModel.isLoading.collectAsState().value
    val spinnerTextState = registrationViewModel.spinnerText.collectAsState().value

    val snackBarHostState = remember { registrationViewModel.snackBarHostState }

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val phone = remember { mutableStateOf("") }
    val code = remember { mutableStateOf("") }

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
            selectedOption = selectedOptionState,
            email = email,
            password = password,
            phone = phone,
            code = code,
            fieldErrors = fieldErrorsState,
        )

        Spacer(modifier = Modifier.height(30.dp))
        AuthOptions(
            options = authOptionsState,
            title = "Выбор варианта регистрации",
            onClick = { item -> registrationViewModel.selectOption(item) },
        )

        Spacer(modifier = Modifier.height(20.dp))
        InfoCard(
            icon = Icon.ResourceIcon(R.drawable.i_info),
            title = "Важно!",
            text = registrationViewModel.getSelectedOptionDescription(selectedOptionState),
            colors = CardDefaults.defaultCardColors(),
        )

        Spacer(modifier = Modifier.padding(bottom = 120.dp))
    }

    BottomContainer {
        Spacer(modifier = Modifier.height(20.dp))
        SimpleButton(text = "Продолжить") {
            when (selectedOptionState) {
                AuthVariantsEnum.EMAIL -> {
                    registrationViewModel.registrationWithEmailAndPassword(
                        email = email.value,
                        password = password.value,
                        navigateToHomeScreen = navigateToHomeScreen,
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
