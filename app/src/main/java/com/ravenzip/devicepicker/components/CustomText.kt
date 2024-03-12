package com.ravenzip.devicepicker.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun CustomText(text: String, size: Int = 14, weight: FontWeight = FontWeight.W400) {
    Text(
        text = text,
        modifier = Modifier.fillMaxWidth(),
        fontSize = size.sp,
        fontWeight = weight,
        textAlign = TextAlign.Start
    )
}
