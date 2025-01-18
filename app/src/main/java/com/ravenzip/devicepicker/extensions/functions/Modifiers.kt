package com.ravenzip.devicepicker.extensions.functions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Контейнер для маленького изображения */
fun Modifier.smallImageContainer(
    shape: Dp = 10.dp,
    color: Color = Color.White,
    padding: PaddingValues = PaddingValues(vertical = 15.dp, horizontal = 10.dp),
    width: Dp = 140.dp,
    height: Dp = 140.dp,
) =
    this then
        (this.clip(RoundedCornerShape(shape))
            .background(color)
            .padding(padding)
            .size(width, height))

/** Контейнер для большого изображения */
fun Modifier.bigImageContainer(
    height: Dp = 400.dp,
    color: Color = Color.White,
    padding: PaddingValues = PaddingValues(vertical = 15.dp, horizontal = 15.dp),
) = this then (this.fillMaxWidth().height(height).background(color).padding(padding))
