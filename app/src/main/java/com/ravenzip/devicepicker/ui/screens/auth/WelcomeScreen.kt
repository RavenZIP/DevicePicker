package com.ravenzip.devicepicker.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.constants.enums.WelcomeEnum
import com.ravenzip.devicepicker.extensions.functions.inverseColors
import com.ravenzip.devicepicker.extensions.functions.showError
import com.ravenzip.devicepicker.state.UiState
import com.ravenzip.devicepicker.viewmodels.auth.WelcomeScreenViewModel
import com.ravenzip.workshop.components.AlertDialog
import com.ravenzip.workshop.components.HorizontalPagerIndicator
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.components.SnackBar
import com.ravenzip.workshop.components.Spinner
import com.ravenzip.workshop.data.icon.Icon

@Composable
fun WelcomeScreen(
    welcomeScreenViewModel: WelcomeScreenViewModel = hiltViewModel<WelcomeScreenViewModel>(),
    navigateToRegistrationScreen: () -> Unit,
    navigateToLoginScreen: () -> Unit,
    navigateToHomeScreen: () -> Unit,
) {
    val uiState =
        welcomeScreenViewModel.uiState.collectAsStateWithLifecycle(UiState.Nothing()).value

    val snackBarHostState = remember { SnackbarHostState() }
    val pagerState = rememberPagerState(pageCount = { 4 })

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { content ->
            when (content) {
                0 -> {
                    ScreenContent(
                        imageId = R.drawable.devices,
                        title = WelcomeEnum.MORE_INFORMATION.title,
                        text = WelcomeEnum.MORE_INFORMATION.text,
                    )
                }
                1 -> {
                    ScreenContent(
                        imageId = R.drawable.search,
                        title = WelcomeEnum.ADVANCED_SEARCH.title,
                        text = WelcomeEnum.ADVANCED_SEARCH.text,
                    )
                }
                2 -> {
                    ScreenContent(
                        imageId = R.drawable.time,
                        title = WelcomeEnum.OPTIONAL_REGISTRATION.title,
                        text = WelcomeEnum.OPTIONAL_REGISTRATION.text,
                    )
                }
                3 -> {
                    ScreenContent(
                        imageId = R.drawable.devicepicker,
                        title = WelcomeEnum.DEVICE_PICKER.title,
                        text = WelcomeEnum.DEVICE_PICKER.text,
                        isFinal = true,
                        navigateToRegistrationScreen = navigateToRegistrationScreen,
                        navigateToLoginScreen = navigateToLoginScreen,
                        continueWithoutAuthClick = {
                            welcomeScreenViewModel.alertDialog.showDialog()
                        },
                    )
                }
            }
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            HorizontalPagerIndicator(
                pagerState,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                MaterialTheme.colorScheme.primary,
                height = 10,
                width = 20,
            )
        }
    }

    when (uiState) {
        is UiState.Loading -> {
            Spinner(text = "Анонимный вход...")
        }

        is UiState.Dialog.Opened -> {
            AlertDialog(
                icon = Icon.ResourceIcon(R.drawable.sign_in),
                title = WelcomeEnum.DIALOG_WINDOW.title,
                text = WelcomeEnum.DIALOG_WINDOW.text,
                onDismissText = "Назад",
                onConfirmationText = "Продолжить",
                onDismiss = { welcomeScreenViewModel.alertDialog.hideDialog() },
                onConfirmation = { welcomeScreenViewModel.alertDialog.onDialogConfirmation() },
            )
        }

        else -> {
            // do nothing
        }
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is UiState.Error -> {
                snackBarHostState.showError(uiState.message)
            }

            is UiState.Dialog.Confirmed -> {
                navigateToHomeScreen()
            }

            else -> {
                // do nothing
            }
        }
    }

    SnackBar(snackBarHostState = snackBarHostState)
}

@Composable
private fun ScreenContent(
    imageId: Int,
    title: String,
    text: String,
    isFinal: Boolean = false,
    navigateToRegistrationScreen: () -> Unit = {},
    navigateToLoginScreen: () -> Unit = {},
    continueWithoutAuthClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(painter = painterResource(imageId), contentDescription = "")
        Spacer(modifier = Modifier.height(80.dp))

        Column(
            modifier = Modifier.fillMaxWidth(0.9f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                fontSize = if (isFinal) 24.sp else 22.sp,
                fontWeight = FontWeight.W500,
                letterSpacing = 0.sp,
            )
            Spacer(modifier = Modifier.height(if (isFinal) 10.dp else 20.dp))
            Text(text = text, letterSpacing = 0.sp, textAlign = TextAlign.Center)
        }

        if (isFinal) {
            Spacer(modifier = Modifier.height(40.dp))
            SimpleButton(text = "Регистрация") { navigateToRegistrationScreen() }

            Spacer(modifier = Modifier.height(20.dp))
            SimpleButton(text = "Вход") { navigateToLoginScreen() }

            Spacer(modifier = Modifier.height(20.dp))
            SimpleButton(
                text = "Продолжить без регистрации",
                colors = ButtonDefaults.inverseColors(),
            ) {
                continueWithoutAuthClick()
            }
        }
    }
}
