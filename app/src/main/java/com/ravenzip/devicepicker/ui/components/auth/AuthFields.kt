package com.ravenzip.devicepicker.ui.components.auth

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
import com.ravenzip.workshop.components.SinglenessTextField
import com.ravenzip.workshop.data.Error
import com.ravenzip.workshop.data.IconParameters

@Composable
fun GetFields(
    selectedVariant: () -> AuthEnum,
    fields: List<MutableState<String>>,
    validation: List<Boolean>
) {
    when (selectedVariant()) {
        AuthEnum.EMAIL -> {
            SinglenessTextField(
                text = fields[0],
                label = "Электронная почта",
                leadingIcon =
                    IconParameters(value = ImageVector.vectorResource(R.drawable.i_email), size = 20),
                error = Error(value = !validation[0])
            )

            Spacer(modifier = Modifier.height(15.dp))
            SinglenessTextField(
                text = fields[1],
                label = "Пароль",
                leadingIcon = IconParameters(value = ImageVector.vectorResource(R.drawable.i_key), size = 20),
                isHiddenText = true,
                error = Error(value = !validation[1])
            )
        }
        AuthEnum.PHONE -> {
            SinglenessTextField(
                text = fields[0],
                label = "Телефон",
                leadingIcon =
                    IconParameters(value = ImageVector.vectorResource(R.drawable.i_phone), size = 20),
                error = Error(value = !validation[0])
            )
        }
        AuthEnum.GOOGLE -> {
            Text(text = "Хз че тут пока что")
        }
    }
}
