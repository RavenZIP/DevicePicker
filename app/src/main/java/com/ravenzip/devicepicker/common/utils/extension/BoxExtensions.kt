package com.ravenzip.devicepicker.common.utils.extension

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ColoredBoxWithBorder(color: Color) {
    Box(
        modifier = Modifier.size(16.dp).border(1.dp, color, shape = RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center) {
            Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(2.dp)).background(color))
        }
}
