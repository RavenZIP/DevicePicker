package com.ravenzip.devicepicker.state

sealed class State {
    data object Nothing : State()

    data object Loading : State()

    data object Dialog : State()

    data object Success : State()

    data class Error(val message: String) : State()
}
