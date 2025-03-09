package com.ravenzip.devicepicker.common.model.result

sealed class Result<out T> {
    data class Success<T>(val value: T) : Result<T>()

    sealed class Error(open val message: String) : Result<Nothing>() {
        data class Default(override val message: String) : Error(message)

        data class Network(override val message: String) : Error(message)
    }
}
