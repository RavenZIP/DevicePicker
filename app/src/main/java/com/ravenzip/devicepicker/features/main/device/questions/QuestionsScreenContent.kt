package com.ravenzip.devicepicker.features.main.device.questions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
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
import com.ravenzip.devicepicker.common.utils.extension.inverseColors
import com.ravenzip.devicepicker.common.utils.extension.veryLightPrimary
import com.ravenzip.workshop.components.Icon
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.data.TextConfig
import com.ravenzip.workshop.data.icon.IconConfig
import com.ravenzip.workshop.data.icon.IconData

// TODO пока просто макет, потом реализовать подтягивание вопросов из БД
@Composable
fun QuestionsScreenContent(padding: PaddingValues) {
    LazyColumn(
        modifier = Modifier.padding(padding).fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(top = 10.dp),
    ) {
        item { QuestionCard() }

        item { QuestionCard() }

        item { QuestionCard() }
    }
}

@Composable
private fun QuestionCard() {
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
                Text(
                    text = "Заголовок вопроса",
                    fontWeight = FontWeight.W500,
                    fontSize = 20.sp,
                    letterSpacing = 0.sp,
                )
                Text(text = "Текст вопроса", letterSpacing = 0.sp)
            }

            SimpleButton(
                width = 1f,
                text = "Ответить",
                textConfig = TextConfig.SmallCenteredMedium,
                contentPadding = PaddingValues(5.dp),
                colors = ButtonDefaults.inverseColors(),
            )
        }
    }
}
