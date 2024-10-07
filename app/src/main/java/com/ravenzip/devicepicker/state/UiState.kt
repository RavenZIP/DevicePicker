package com.ravenzip.devicepicker.state

sealed class UiState<out T> {
    class Nothing : UiState<Nothing>()

    data class Loading<T>(val message: String) : UiState<T>()

    data class Success<T>(val data: T) : UiState<T>()

    sealed class Dialog : UiState<Nothing>() {
        class Opened() : Dialog()

        class Confirmed() : Dialog()
    }

    data class Error<T>(val message: String) : UiState<T>()
}
