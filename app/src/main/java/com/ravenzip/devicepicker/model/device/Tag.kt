package com.ravenzip.devicepicker.model.device

class Tag<T, R>(val computedValue: T, val manualValue: R) {
    companion object {
        fun createDoubleWithBooleanTag(): Tag<Double, Boolean> {
            return Tag(computedValue = 0.0, manualValue = false)
        }

        fun createBooleanTag(): Tag<Boolean, Boolean> {
            return Tag(computedValue = false, manualValue = false)
        }
    }
}
