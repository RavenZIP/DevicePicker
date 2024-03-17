package com.ravenzip.devicepicker.extensions.functions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.imageContainer(
    shape: Dp = 10.dp,
    color: Color,
    padding: PaddingValues,
    width: Dp,
    height: Dp
) =
    this then
        (this.clip(RoundedCornerShape(shape))
            .background(color)
            .padding(padding)
            .width(width)
            .height(height))
