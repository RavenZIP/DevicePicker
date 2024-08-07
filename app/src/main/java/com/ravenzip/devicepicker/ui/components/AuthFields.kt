package com.ravenzip.devicepicker.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.constants.enums.AuthVariantsEnum
import com.ravenzip.workshop.components.SinglenessTextField
import com.ravenzip.workshop.data.Error
import com.ravenzip.workshop.data.IconConfig

@Composable
fun GetFields(
    selectedVariant: () -> AuthVariantsEnum,
    fields: List<MutableState<String>>,
    vararg validation: Error
) {
    when (selectedVariant()) {
        AuthVariantsEnum.EMAIL -> {
            SinglenessTextField(
                text = fields[0],
                label = "Электронная почта",
                leadingIcon =
                    IconConfig(value = ImageVector.vectorResource(R.drawable.i_email), size = 20),
                error = validation[0])

            Spacer(modifier = Modifier.height(15.dp))
            SinglenessTextField(
                text = fields[1],
                label = "Пароль",
                leadingIcon =
                    IconConfig(value = ImageVector.vectorResource(R.drawable.i_key), size = 20),
                isHiddenText = true,
                error = validation[1])
        }
        AuthVariantsEnum.PHONE -> {
            SinglenessTextField(
                text = fields[0],
                label = "Телефон",
                leadingIcon =
                    IconConfig(value = ImageVector.vectorResource(R.drawable.i_phone), size = 20),
                error = validation[0])
        }
        AuthVariantsEnum.GOOGLE -> {
            Text(text = "Хз че тут пока что")
        }
    }
}
