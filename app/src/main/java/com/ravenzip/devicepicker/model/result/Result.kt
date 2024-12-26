package com.ravenzip.devicepicker.model.result

import com.ravenzip.devicepicker.constants.enums.StatusEnum

class Result<T>(val value: T?, val status: StatusEnum, val error: OperationError?) {
    companion object {
        fun <T> default() = Result<T>(null, StatusEnum.UNKNOWN, null)

        fun <T> loading() = Result<T>(null, StatusEnum.LOADING, null)

        fun <T> loading(value: T) = Result(value, StatusEnum.LOADING, null)

        fun <T> success(value: T) = Result(value, StatusEnum.OK, null)

        fun success() = Result(true, StatusEnum.OK, null)

        fun <T> error(errorMessage: String) =
            Result<T>(null, StatusEnum.ERROR, OperationError.default(errorMessage))

        fun <T> error(value: T, errorMessage: String) =
            Result(value, StatusEnum.ERROR, OperationError.default(errorMessage))

        fun <T> networkError(errorMessage: String) =
            Result<T>(null, StatusEnum.ERROR, OperationError.networkError(errorMessage))

        fun <T> networkError(value: T, errorMessage: String) =
            Result(value, StatusEnum.ERROR, OperationError.networkError(errorMessage))
    }
}
