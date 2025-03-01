package com.ravenzip.devicepicker.ui.screens.main.user.company

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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

// TODO придумать наименование и, возможно, вынести в библиотеку
@Composable
fun InfoCard2(text: String, description: String, icon: ImageVector, iconColor: Color) {
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

// TODO убрать после обновления WorkShop
class SpinnerState(val isLoading: Boolean, val text: String)
