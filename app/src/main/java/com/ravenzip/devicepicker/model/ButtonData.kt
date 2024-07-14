package com.ravenzip.devicepicker.model

import androidx.compose.ui.graphics.vector.ImageVector

class ButtonData(
    val icon: ImageVector,
    val value: String,
    val text: String,
    val onClick: () -> Unit
)
