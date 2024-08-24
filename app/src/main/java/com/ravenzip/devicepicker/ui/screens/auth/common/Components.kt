package com.ravenzip.devicepicker.ui.screens.auth.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.constants.enums.AuthVariantsEnum
import com.ravenzip.devicepicker.extensions.functions.defaultCardColors
import com.ravenzip.workshop.components.RadioGroup
import com.ravenzip.workshop.components.SinglenessTextField
import com.ravenzip.workshop.data.Error
import com.ravenzip.workshop.data.selection.SelectionConfig

@Composable
fun LoginAndRegistrationFields(
    selectedVariant: () -> AuthVariantsEnum,
    email: MutableState<String>,
    emailError: MutableState<Error>,
    password: MutableState<String>,
    passwordError: MutableState<Error>,
    phone: MutableState<String>,
    phoneError: MutableState<Error>,
    code: MutableState<String>,
    codeError: MutableState<Error>,
) {
    when (selectedVariant()) {
        AuthVariantsEnum.EMAIL -> {
            SinglenessTextField(
                text = email,
                label = "Электронная почта",
                leadingIcon = ImageVector.vectorResource(R.drawable.i_email),
                error = emailError.value,
            )

            Spacer(modifier = Modifier.height(15.dp))
            SinglenessTextField(
                text = password,
                label = "Пароль",
                leadingIcon = ImageVector.vectorResource(R.drawable.i_key),
                isHiddenText = true,
                error = passwordError.value,
            )
        }
        AuthVariantsEnum.PHONE -> {
            SinglenessTextField(
                text = phone,
                label = "Телефон",
                leadingIcon = ImageVector.vectorResource(R.drawable.i_phone),
                error = phoneError.value,
            )

            // TODO добавить поле с кодом
        }
        AuthVariantsEnum.GOOGLE -> {
            // TODO
        }
    }
}

@Composable
fun LoginAndRegistrationOptions(options: SnapshotStateList<SelectionConfig>, title: String) {
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
        RadioGroup(width = 1f, list = options, textSize = 16)
    }
}
