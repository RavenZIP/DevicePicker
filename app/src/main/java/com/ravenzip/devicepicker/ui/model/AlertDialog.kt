package com.ravenzip.devicepicker.ui.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update

class AlertDialog {
    private val _state = MutableStateFlow(AlertDialogAction.HIDDEN)

    val state = _state.asStateFlow()

    val isShown = state.filter { state -> state == AlertDialogAction.OPENED }
    val isHidden = state.filter { state -> state == AlertDialogAction.HIDDEN }
    val isConfirmed = state.filter { state -> state == AlertDialogAction.CONFIRMED }

    fun showDialog() {
        _state.update { AlertDialogAction.OPENED }
    }

    fun hideDialog() {
        _state.update { AlertDialogAction.HIDDEN }
    }

    fun onDialogConfirmation() {
        _state.update { AlertDialogAction.CONFIRMED }
    }
}
