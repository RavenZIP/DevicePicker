package com.ravenzip.devicepicker.model.result

import com.ravenzip.devicepicker.constants.enums.OperationErrorTypeEnum

class OperationError(val message: String, val type: OperationErrorTypeEnum) {
    companion object {
        fun default(message: String) =
            OperationError(message = message, type = OperationErrorTypeEnum.DEFAULT)

        fun networkError(message: String) =
            OperationError(message = message, type = OperationErrorTypeEnum.NETWORK_ERROR)
    }
}
