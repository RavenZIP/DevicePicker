package com.ravenzip.devicepicker.ui.components.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ravenzip.workshop.components.SinglenessTextField

@Composable
fun GetFields(selectedVariant: () -> String, fields: List<MutableState<String>>) {
    when (selectedVariant()) {
        AuthEnum.EMAIL.value -> {
            SinglenessTextField(text = fields[0], label = "Электронная почта")

            Spacer(modifier = Modifier.height(15.dp))
            SinglenessTextField(text = fields[1], label = "Пароль")
        }
        AuthEnum.PHONE.value -> {
            SinglenessTextField(text = fields[0], label = "Телефон")
        }
        AuthEnum.GOOGLE.value -> {
            Text(text = "Хз че тут пока что")
        }
        else -> {}
    }
}