package com.ravenzip.devicepicker.common.model

import androidx.compose.runtime.Immutable

@Immutable
class ButtonData(val iconId: Int, val value: String, val text: String, val onClick: () -> Unit)
