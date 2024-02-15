package com.ravenzip.devicepicker.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravenzip.devicepicker.ui.components.BottomContainer
import com.ravenzip.workshop.components.InfoCard
import com.ravenzip.workshop.components.RadioGroup
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.components.SinglenessTextField
import com.ravenzip.workshop.data.IconParameters
import com.ravenzip.workshop.data.SelectionParameters
import com.ravenzip.workshop.data.TextParameters

@Composable
fun RegistrationScreen() {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val phone = remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val registerVariants = remember { generateRegisterVariants() }
    val selectedRegisterVariant = remember {
        { registerVariants.first { value -> value.isSelected }.text }
    }

    Column(
        modifier =
            Modifier.fillMaxSize()
                .clickable(interactionSource = interactionSource, indication = null) {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "Регистрация",
            modifier = Modifier.align(Alignment.Start).padding(start = 20.dp, end = 20.dp),
            fontSize = 25.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.sp
        )

        Spacer(modifier = Modifier.height(30.dp))
        when (selectedRegisterVariant()) {
            "Электронная почта" -> {
                SinglenessTextField(text = email, label = "Электронная почта")

                Spacer(modifier = Modifier.height(15.dp))
                SinglenessTextField(text = password, label = "Пароль")
            }
            "Телефон" -> {
                SinglenessTextField(text = phone, label = "Телефон")
            }
            "Google аккаунт" -> {
                Text(text = "Хз че тут пока что")
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
        Card(
            modifier = Modifier.fillMaxWidth(0.9f),
            shape = RoundedCornerShape(10.dp),
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    contentColor = LocalContentColor.current
                ),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Spacer(modifier = Modifier.padding(top = 20.dp))
            Text(
                text = "Выбор варианта регистрации",
                modifier = Modifier.padding(start = 15.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.W500
            )
            Spacer(modifier = Modifier.padding(top = 10.dp))
            RadioGroup(width = 1f, list = registerVariants, textSize = 16)
        }

        Spacer(modifier = Modifier.height(20.dp))
        InfoCard(
            icon =
                IconParameters(
                    value = Icons.Outlined.Info,
                    color = MaterialTheme.colorScheme.primary
                ),
            title = TextParameters(value = "Важно!", size = 20),
            text = TextParameters(value = getCardText(selectedRegisterVariant()), size = 14),
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    contentColor = LocalContentColor.current
                )
        )

        Spacer(modifier = Modifier.padding(bottom = 120.dp))
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

private fun generateRegisterVariants(): SnapshotStateList<SelectionParameters> {
    val email = SelectionParameters(isSelected = true, text = "Электронная почта")
    val phone = SelectionParameters(isSelected = false, text = "Телефон")
    val google = SelectionParameters(isSelected = false, text = "Google аккаунт")

    return mutableStateListOf(email, phone, google)
}

private fun getCardText(selectedRegisterVariant: String): String {
    return when (selectedRegisterVariant) {
        "Электронная почта" -> WithEmailDescription
        "Телефон" -> WithPhoneDescription
        "Google аккаунт" -> WithGoogleDescription
        else -> ""
    }
}
