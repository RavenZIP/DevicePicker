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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravenzip.devicepicker.R
import com.ravenzip.workshop.components.HorizontalPagerIndicator
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.data.TextParameters

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WelcomeScreen() {
    val pagerState = rememberPagerState(pageCount = { 4 })
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
                        isFinal = false
                    )
                }
                1 -> {
                    ScreenContent(
                        image = painterResource(id = R.drawable.search),
                        title = "Продвинутый поиск",
                        text =
                            "Возможность поиска устройства по генерируемым меткам. Благодаря этому " +
                                "поиск можно сделать точнее или найти необходимое быстрее",
                        isFinal = false
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
                        isFinal = false
                    )
                }
                3 -> {
                    ScreenContent(
                        image = painterResource(id = R.drawable.devicepicker),
                        title = "Device Picker",
                        text = "Лучшее приложение для подбора переносных корпоративных устройств",
                        isFinal = true
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
}

@Composable
private fun ScreenContent(image: Painter, title: String, text: String, isFinal: Boolean) {
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
            ) {}

            Spacer(modifier = Modifier.height(20.dp))
            SimpleButton(text = TextParameters("Вход", size = 16), textAlign = TextAlign.Center) {}

            Spacer(modifier = Modifier.height(20.dp))
            SimpleButton(
                text = TextParameters("Продолжить без регистрации", size = 16),
                textAlign = TextAlign.Center,
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
            ) {}
        }
    }
}
