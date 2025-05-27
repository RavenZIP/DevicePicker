package com.ravenzip.devicepicker.features.main.device.reviews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.common.components.CenterRow
import com.ravenzip.devicepicker.common.components.RatingBar
import com.ravenzip.devicepicker.common.components.SmallText
import com.ravenzip.devicepicker.common.utils.extension.veryLightPrimary
import com.ravenzip.workshop.components.Icon
import com.ravenzip.workshop.data.icon.IconConfig
import com.ravenzip.workshop.data.icon.IconData

// TODO пока просто макет, потом реализовать подтягивание отзывов из БД
@Composable
fun ReviewsScreenContent(padding: PaddingValues) {
    LazyColumn(
        modifier = Modifier.padding(padding).fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(top = 10.dp),
    ) {
        item { Text(text = "Оценка", modifier = Modifier.fillMaxWidth(0.9f), fontSize = 18.sp) }

        item {
            Card(
                Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.veryLightPrimary(),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.9f).padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    CenterRow {
                        Text(text = "${4.83} ", fontWeight = FontWeight.W500, fontSize = 23.sp)
                        Text(text = "из 5", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        RatingBar(4.83.toFloat(), itemSize = 20)
                    }

                    CenterRow {
                        Text(
                            text = "Количество отзывов:",
                            fontWeight = FontWeight.W500,
                            letterSpacing = 0.sp,
                        )
                        Text(
                            text = "${510}",
                            modifier = Modifier.padding(start = 5.dp),
                            letterSpacing = 0.sp,
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(5.dp)) }

        item { Text(text = "Отзывы", modifier = Modifier.fillMaxWidth(0.9f), fontSize = 18.sp) }

        item { ReviewCard() }

        item { ReviewCard() }

        item { ReviewCard() }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
private fun ReviewCard() {
    Card(
        Modifier.fillMaxWidth(0.9f),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.veryLightPrimary(),
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            CenterRow {
                Icon(
                    icon = IconData.ResourceIcon(R.drawable.i_user),
                    iconConfig = IconConfig(size = 35),
                )
                Spacer(modifier = Modifier.width(20.dp))
                Column {
                    Text(text = "Владимир П.")
                    Text(text = "27.05.2025")
                }
            }

            Column {
                SmallText(text = "Оценка", fontWeight = FontWeight.W500, letterSpacing = 0.sp)
                Spacer(modifier = Modifier.height(5.dp))
                RatingBar(5.00.toFloat())
                Spacer(modifier = Modifier.height(5.dp))
            }

            Column {
                SmallText(text = "Метки", fontWeight = FontWeight.W500, letterSpacing = 0.sp)
                SmallText(text = "Пользователь не указал меток", letterSpacing = 0.sp)
            }

            Column {
                SmallText(
                    text = "Срок использования",
                    fontWeight = FontWeight.W500,
                    letterSpacing = 0.sp,
                )
                SmallText(text = "Менее месяца", letterSpacing = 0.sp)
            }

            Column {
                SmallText(text = "Достоинства", fontWeight = FontWeight.W500, letterSpacing = 0.sp)
                SmallText(text = "....", letterSpacing = 0.sp)
            }

            Column {
                SmallText(text = "Недостатки", fontWeight = FontWeight.W500, letterSpacing = 0.sp)
                SmallText(text = "...", letterSpacing = 0.sp)
            }

            Column {
                SmallText(text = "Комментарий", fontWeight = FontWeight.W500, letterSpacing = 0.sp)
                SmallText(text = "...", letterSpacing = 0.sp)
            }
        }
    }
}
