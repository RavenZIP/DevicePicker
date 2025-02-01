package com.ravenzip.devicepicker.ui.screens.main.user.company

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravenzip.devicepicker.extensions.functions.inverseColors
import com.ravenzip.devicepicker.viewmodels.user.CompanyViewModel
import com.ravenzip.workshop.components.SimpleButton

@Composable
fun CompanyScreenContent(viewModel: CompanyViewModel, padding: PaddingValues) {
    Column(
        modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text =
                "Перед началом работы с разделом \"Компания\" необходимо создать свою компанию, либо вступить в уже существующую. " +
                    "Пожалуйста, одно их двух действий ниже:",
            modifier = Modifier.fillMaxWidth(0.9f),
            letterSpacing = 0.sp,
        )

        Spacer(modifier = Modifier.height(25.dp))

        ActionCard(
            title = "Создать компанию",
            description =
                "Создайте собственную компанию, чтобы добавлять сотрудников и управлять устройствами",
            textColor = MaterialTheme.colorScheme.surface,
            buttonText = "Создать компанию",
            buttonColors = ButtonDefaults.inverseColors(),
            containerColor = MaterialTheme.colorScheme.primary,
        )

        Spacer(modifier = Modifier.height(25.dp))
        Text("или", fontSize = 22.sp, fontWeight = FontWeight.W500)
        Spacer(modifier = Modifier.height(25.dp))

        ActionCard(
            title = "Вступить в уже существующую",
            description =
                "Присоединитесь к существующей компании, чтобы получить доступ к общим ресурсам и работать в единой команде",
            buttonText = "Вступить в компанию",
        )

        Spacer(modifier = Modifier.height(25.dp))
    }
}

@Composable
fun ActionCard(
    title: String,
    description: String,
    textColor: Color = Color.Unspecified,
    buttonText: String,
    buttonColors: ButtonColors = ButtonDefaults.buttonColors(),
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    onClick: () -> Unit = {},
) {
    Column(
        modifier =
            Modifier.fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(10.dp))
                .background(containerColor)
                .padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            title,
            fontSize = 23.sp,
            fontWeight = FontWeight.W500,
            color = textColor,
            letterSpacing = 0.sp,
        )
        Text(description, fontSize = 16.sp, color = textColor, letterSpacing = 0.sp)
        SimpleButton(width = 1f, text = buttonText, colors = buttonColors, onClick = onClick)
    }
}
