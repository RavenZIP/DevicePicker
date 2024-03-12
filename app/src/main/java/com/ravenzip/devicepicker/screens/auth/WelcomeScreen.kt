package com.ravenzip.devicepicker.screens.auth

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.enums.WelcomeEnum
import com.ravenzip.devicepicker.extensions.functions.getInverseColors
import com.ravenzip.devicepicker.services.logInAnonymously
import com.ravenzip.devicepicker.services.reloadUser
import com.ravenzip.devicepicker.services.showError
import com.ravenzip.workshop.components.AlertDialog
import com.ravenzip.workshop.components.HorizontalPagerIndicator
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.components.SnackBar
import com.ravenzip.workshop.components.Spinner
import com.ravenzip.workshop.data.IconParameters
import com.ravenzip.workshop.data.TextParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WelcomeScreen(
    navigateToRegistrationScreen: () -> Unit,
    navigateToLoginScreen: () -> Unit,
    navigateToHomeScreen: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 4 })
    val alertDialogIsShown = remember { mutableStateOf(false) }
    val isLoading = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) {
            when (it) {
                0 -> {
                    ScreenContent(
                        image = painterResource(id = R.drawable.devices),
                        title = WelcomeEnum.MORE_INFORMATION.title,
                        text = WelcomeEnum.MORE_INFORMATION.text
                    )
                }
                1 -> {
                    ScreenContent(
                        image = painterResource(id = R.drawable.search),
                        title = WelcomeEnum.ADVANCED_SEARCH.title,
                        text = WelcomeEnum.ADVANCED_SEARCH.text,
                    )
                }
                2 -> {
                    ScreenContent(
                        image = painterResource(id = R.drawable.time),
                        title = WelcomeEnum.OPTIONAL_REGISTRATION.title,
                        text = WelcomeEnum.OPTIONAL_REGISTRATION.text,
                    )
                }
                3 -> {
                    ScreenContent(
                        image = painterResource(id = R.drawable.devicepicker),
                        title = WelcomeEnum.DEVICE_PICKER.title,
                        text = WelcomeEnum.DEVICE_PICKER.text,
                        isFinal = true,
                        navigateToRegistrationScreen = navigateToRegistrationScreen,
                        navigateToLoginScreen = navigateToLoginScreen,
                        continueWithoutAuthClick = { alertDialogIsShown.value = true }
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
                width = 20
            )
        }
    }

    if (alertDialogIsShown.value) {
        AlertDialog(
            icon = IconParameters(value = ImageVector.vectorResource(R.drawable.sign_in)),
            title = TextParameters(value = WelcomeEnum.DIALOG_WINDOW.title, size = 22),
            text = TextParameters(value = WelcomeEnum.DIALOG_WINDOW.text, size = 14),
            onDismissText = TextParameters(value = "Назад", size = 14),
            onConfirmationText = TextParameters(value = "Продолжить", size = 14),
            onDismiss = { alertDialogIsShown.value = false },
            onConfirmation = {
                scope.launch(Dispatchers.Main) {
                    isLoading.value = true

                    val isReloadSuccess = reloadUser()
                    if (isReloadSuccess.value != true) {
                        isLoading.value = false
                        alertDialogIsShown.value = false
                        snackBarHostState.showError(isReloadSuccess.error!!)
                        return@launch
                    }

                    val authResult = logInAnonymously()
                    isLoading.value = false
                    alertDialogIsShown.value = false

                    if (authResult !== null) navigateToHomeScreen()
                    else snackBarHostState.showError("Произошла ошибка при выполнении запроса")
                }
            }
        )
    }

    if (isLoading.value) {
        Spinner(text = TextParameters(value = "Авторизация...", size = 16))
    }

    SnackBar(snackBarHostState = snackBarHostState)
}

@Composable
private fun ScreenContent(
    image: Painter,
    title: String,
    text: String,
    isFinal: Boolean = false,
    navigateToRegistrationScreen: () -> Unit = {},
    navigateToLoginScreen: () -> Unit = {},
    continueWithoutAuthClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(painter = image, contentDescription = "")
        Spacer(modifier = Modifier.height(80.dp))

        Column(
            modifier = Modifier.fillMaxWidth(0.9f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = if (isFinal) 24.sp else 22.sp,
                fontWeight = FontWeight.W500,
                letterSpacing = 0.sp
            )
            Spacer(modifier = Modifier.height(if (isFinal) 10.dp else 20.dp))
            Text(text = text, letterSpacing = 0.sp, textAlign = TextAlign.Center)
        }

        if (isFinal) {
            Spacer(modifier = Modifier.height(40.dp))
            SimpleButton(
                text = TextParameters("Регистрация", size = 16),
                textAlign = TextAlign.Center
            ) {
                navigateToRegistrationScreen()
            }

            Spacer(modifier = Modifier.height(20.dp))
            SimpleButton(text = TextParameters("Вход", size = 16), textAlign = TextAlign.Center) {
                navigateToLoginScreen()
            }

            Spacer(modifier = Modifier.height(20.dp))
            SimpleButton(
                text = TextParameters("Продолжить без регистрации", size = 16),
                textAlign = TextAlign.Center,
                colors = getInverseColors()
            ) {
                continueWithoutAuthClick()
            }
        }
    }
}
