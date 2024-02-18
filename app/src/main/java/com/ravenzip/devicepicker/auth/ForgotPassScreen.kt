package com.ravenzip.devicepicker.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravenzip.devicepicker.ui.components.BottomContainer
import com.ravenzip.devicepicker.ui.components.default.getDefaultColors
import com.ravenzip.workshop.components.InfoCard
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.components.SinglenessTextField
import com.ravenzip.workshop.data.IconParameters
import com.ravenzip.workshop.data.TextParameters

@Composable
fun ForgotPasswordScreen() {
    val email = remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column(
        modifier =
            Modifier.fillMaxSize().clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                focusManager.clearFocus()
                keyboardController?.hide()
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "Сброс и восстановление",
            modifier = Modifier.align(Alignment.Start).padding(start = 20.dp, end = 20.dp),
            fontSize = 25.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.sp
        )

        Spacer(modifier = Modifier.height(30.dp))
        SinglenessTextField(text = email, label = "Электронная почта")

        Spacer(modifier = Modifier.height(30.dp))
        InfoCard(
            icon =
                IconParameters(
                    value = Icons.Outlined.Info,
                    color = MaterialTheme.colorScheme.primary
                ),
            title = TextParameters(value = "Заголовок", size = 20),
            text =
                TextParameters(
                    value = "Длиннющий текст описания карточки, чтобы увидеть как она выглядит",
                    size = 14
                ),
            colors = getDefaultColors()
        )
    }

    BottomContainer {
        Spacer(modifier = Modifier.height(20.dp))
        SimpleButton(
            text = TextParameters(value = "Продолжить", size = 16),
            textAlign = TextAlign.Center
        ) {}
        Spacer(modifier = Modifier.height(20.dp))
    }
}
