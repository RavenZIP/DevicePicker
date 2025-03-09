package com.ravenzip.devicepicker.common.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class AlertDialog {
    private val _state = MutableStateFlow<DialogState>(DialogState.Dismissed())
    val state = _state.asStateFlow()

    val isShowed = _state.map { currentState -> currentState is DialogState.Showed }
    val isConfirmed = _state.filter { currentState -> currentState is DialogState.Confirmed }

    fun show() {
        _state.update { DialogState.Showed() }
    }

    fun dismiss() {
        _state.update { DialogState.Dismissed() }
    }

    fun confirm() {
        _state.update { DialogState.Confirmed() }
    }
}
