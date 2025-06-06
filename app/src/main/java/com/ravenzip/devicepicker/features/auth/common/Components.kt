package com.ravenzip.devicepicker.features.auth.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.common.enums.AuthTypeEnum
import com.ravenzip.devicepicker.common.utils.extension.defaultCardColors
import com.ravenzip.workshop.components.RadioGroup
import com.ravenzip.workshop.components.SinglenessOutlinedTextField
import com.ravenzip.workshop.data.icon.IconData
import com.ravenzip.workshop.forms.control.FormControl

@Composable
fun AuthFields(
    selectedOption: AuthTypeEnum,
    emailControl: FormControl<String>,
    passwordControl: FormControl<String>,
    phoneControl: FormControl<String>,
    codeControl: FormControl<String>,
) {
    when (selectedOption) {
        AuthTypeEnum.EMAIL -> {
            SinglenessOutlinedTextField(
                control = emailControl,
                label = "Электронная почта",
                leadingIcon = IconData.ResourceIcon(R.drawable.i_email),
            )

            Spacer(modifier = Modifier.height(15.dp))
            SinglenessOutlinedTextField(
                control = passwordControl,
                label = "Пароль",
                leadingIcon = IconData.ResourceIcon(R.drawable.i_key),
                isHiddenText = true,
            )
        }
        AuthTypeEnum.PHONE -> {
            SinglenessOutlinedTextField(
                control = phoneControl,
                label = "Телефон",
                leadingIcon = IconData.ResourceIcon(R.drawable.i_phone),
            )

            Spacer(modifier = Modifier.height(15.dp))
            SinglenessOutlinedTextField(
                control = codeControl,
                label = "СМС-код",
                leadingIcon = IconData.ResourceIcon(R.drawable.i_message),
            )
        }
    }
}

@Composable
fun AuthOptions(control: FormControl<AuthTypeEnum>, title: String) {
    Card(
        modifier = Modifier.fillMaxWidth(0.9f),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.defaultCardColors(),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = title,
            modifier = Modifier.padding(start = 15.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.W500,
        )
        Spacer(modifier = Modifier.height(10.dp))
        RadioGroup(
            control = control,
            source = AuthTypeEnum.entries,
            view = { it.value },
            keySelector = { it },
            width = 1f,
        )
    }
}
