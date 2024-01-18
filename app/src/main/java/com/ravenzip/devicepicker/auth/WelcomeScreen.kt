package com.ravenzip.devicepicker.auth

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravenzip.devicepicker.R
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.data.TextParameters

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WelcomeScreen() {
    val pagerState = rememberPagerState(pageCount = { 4 })
    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) {
            when (it) {
                0 ->
                    ScreenContent(
                        painterResource(id = R.drawable.devices),
                        "Больше информации",
                        "Предоставляем более расширенную информацию о каждом устройстве " +
                            "(нагрев, производительность, качество сборки, надежность)"
                    )
                1 ->
                    ScreenContent(
                        painterResource(id = R.drawable.search),
                        "Продвинутый поиск",
                        "Возможность поиска устройства по меткам. Еще какая-то тонна текста, " +
                            "которую я пока что не придумал, но ее нужно написать, чтобы понять " +
                            "как будет смотреться текст в таком обхеме"
                    )
                2 ->
                    ScreenContent(
                        painterResource(id = R.drawable.time),
                        "Необязательная регистрация",
                        "Регистрация дает возможность писать отзывы, выставлять оценки, " +
                            "хранить один и тот же список избранных товаров на разных устройствах и " +
                            "возможность писать в техподдержку. Если это пока что не нужно, " +
                            "то можно зарегистрироваться позднее"
                    )
                3 ->
                    ChooseScreen(
                        painterResource(id = R.drawable.devicepicker),
                        "Device Picker",
                        "Лучшее приложение для подбора переносных корпоративных устройств"
                    )
            }
        }
        Box(modifier = Modifier.align(Alignment.BottomCenter)) { PagerIndicator(pagerState) }
    }
}

@Composable
private fun ScreenContent(image: Painter, title: String, text: String) {
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
            Text(text = title, fontSize = 22.sp, fontWeight = FontWeight.W500, letterSpacing = 0.sp)
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = text, letterSpacing = 0.sp, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun ChooseScreen(image: Painter, title: String, text: String) {
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
            Text(text = title, fontSize = 24.sp, fontWeight = FontWeight.W500, letterSpacing = 0.sp)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = text, letterSpacing = 0.sp, textAlign = TextAlign.Center)
        }

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
            textAlign = TextAlign.Center
        ) {}
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PagerIndicator(pagerState: PagerState) {
    Row(
        Modifier.fillMaxWidth().padding(bottom = 20.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
            Box(modifier = Modifier.padding(2.dp).clip(CircleShape).background(color).size(10.dp))
        }
    }
}
