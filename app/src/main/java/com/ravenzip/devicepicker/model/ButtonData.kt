package com.ravenzip.devicepicker.model

import androidx.compose.runtime.Immutable

@Immutable
class ButtonData(val iconId: Int, val value: String, val text: String, val onClick: () -> Unit)
