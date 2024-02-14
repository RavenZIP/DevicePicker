package com.ravenzip.devicepicker.auth

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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import com.ravenzip.devicepicker.firebase.reloadUser
import com.ravenzip.devicepicker.firebase.signInAnonymously
import com.ravenzip.workshop.components.AlertDialog
import com.ravenzip.workshop.components.HorizontalPagerIndicator
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.components.Spinner
import com.ravenzip.workshop.data.IconParameters
import com.ravenzip.workshop.data.TextParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WelcomeScreen(
    registrationClick: () -> Unit,
    loginClick: () -> Unit,
    continueWithoutAuthClick: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 4 })
    val alertDialogIsShown = remember { mutableStateOf(false) }
    val isLoading = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) {
            when (it) {
                0 -> {
                    ScreenContent(
                        image = painterResource(id = R.drawable.devices),
                        title = "Больше информации",
                        text =
                            "Предоставляем более расширенную информацию о каждом устройстве " +
                                "(нагрев, производительность, качество сборки, надежность)",
                    )
                }
                1 -> {
                    ScreenContent(
                        image = painterResource(id = R.drawable.search),
                        title = "Продвинутый поиск",
                        text =
                            "Возможность поиска устройства по генерируемым меткам. Благодаря этому " +
                                "поиск можно сделать точнее или найти необходимое быстрее",
                    )
                }
                2 -> {
                    ScreenContent(
                        image = painterResource(id = R.drawable.time),
                        title = "Необязательная регистрация",
                        text =
                            "Регистрация дает возможность писать отзывы, выставлять оценки, " +
                                "хранить один и тот же список избранных товаров на разных устройствах и " +
                                "возможность писать в техподдержку. Если это пока что не нужно, " +
                                "то можно зарегистрироваться позднее",
                    )
                }
                3 -> {
                    ScreenContent(
                        image = painterResource(id = R.drawable.devicepicker),
                        title = "Device Picker",
                        text = "Лучшее приложение для подбора переносных корпоративных устройств",
                        isFinal = true,
                        registrationClick = registrationClick,
                        loginClick = loginClick,
                        continueWithoutAuthClick = { alertDialogIsShown.value = true }
                    )
                }
            }
        }
        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            HorizontalPagerIndicator(
                pagerState,
                MaterialTheme.colorScheme.primary.copy(0.5f),
                MaterialTheme.colorScheme.primary,
                height = 10,
                width = 20
            )
        }
    }

    if (alertDialogIsShown.value) {
        AlertDialog(
            icon = IconParameters(value = ImageVector.vectorResource(R.drawable.sign_in)),
            title = TextParameters("Вход без регистрации", size = 22),
            text =
                TextParameters(
                    "Вы выполняете вход без регистрации. Это значит, " +
                        "что вы потеряете свой список избранных в случае переустановки приложения, " +
                        "не сможете оставлять отзывы и оценки. " +
                        "По истечению месяца ваш аккаунт будет деактивирован!",
                    size = 14
                ),
            onDismissText = TextParameters("Назад", size = 14),
            onConfirmationText = TextParameters("Продолжить", size = 14),
            onDismiss = { alertDialogIsShown.value = false },
            onConfirmation = {
                scope.launch(Dispatchers.Main) {
                    isLoading.value = true
                    reloadUser()
                    signInAnonymously()

                    isLoading.value = false
                    alertDialogIsShown.value = false
                    continueWithoutAuthClick()
                }
            }
        )
    }

    if (isLoading.value) {
        Spinner(text = TextParameters(value = "Авторизация...", size = 16))
    }
}

@Composable
private fun ScreenContent(
    image: Painter,
    title: String,
    text: String,
    isFinal: Boolean = false,
    registrationClick: () -> Unit = {},
    loginClick: () -> Unit = {},
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
                registrationClick()
            }

            Spacer(modifier = Modifier.height(20.dp))
            SimpleButton(text = TextParameters("Вход", size = 16), textAlign = TextAlign.Center) {
                loginClick()
            }

            Spacer(modifier = Modifier.height(20.dp))
            SimpleButton(
                text = TextParameters("Продолжить без регистрации", size = 16),
                textAlign = TextAlign.Center,
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
            ) {
                continueWithoutAuthClick()
            }
        }
    }
}
