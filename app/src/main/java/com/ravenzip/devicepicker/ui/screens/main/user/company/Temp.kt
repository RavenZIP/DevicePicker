package com.ravenzip.devicepicker.ui.screens.main.user.company

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravenzip.devicepicker.extensions.functions.veryLightPrimary
import com.ravenzip.workshop.components.SimpleButton

// TODO Надо ли выносить в библиотеку?
// TODO может быть стоит объединить с EmptyScreenCardWithAction
@Composable
fun EmptyScreenCard(text: String, description: String, icon: ImageVector, iconColor: Color) {
    Card(modifier = Modifier.fillMaxWidth(0.9f), colors = CardDefaults.veryLightPrimary()) {
        Column(
            modifier = Modifier.padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Icon(imageVector = icon, contentDescription = "", tint = iconColor)
                Text(text = text, fontSize = 22.sp, fontWeight = FontWeight.W500)
            }

            Text(text = description)
        }
    }
}

@Composable
fun EmptyScreenCardWithAction(
    text: String,
    description: String,
    icon: ImageVector,
    iconColor: Color,
    buttonText: String,
) {
    Card(modifier = Modifier.fillMaxWidth(0.9f), colors = CardDefaults.veryLightPrimary()) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "",
                    modifier = Modifier.size(22.dp),
                    tint = iconColor,
                )
                Text(text = text, fontSize = 22.sp, fontWeight = FontWeight.W500)
            }

            Text(text = description)

            SimpleButton(width = 1f, text = buttonText)
        }
    }
}

// TODO убрать после обновления WorkShop
class SpinnerState(val isLoading: Boolean, val text: String)
