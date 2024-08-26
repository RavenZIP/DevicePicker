package com.ravenzip.devicepicker.model.result

class Result<T>(val value: T?, val error: OperationError?) {
    companion object {
        fun <T> success(value: T) = Result(value, null)

        fun <T> error(value: T? = null, error: OperationError) = Result(value, error)
    }
}
