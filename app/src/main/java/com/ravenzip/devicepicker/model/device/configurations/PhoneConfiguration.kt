package com.ravenzip.devicepicker.model.device.configurations

class PhoneConfiguration(val randomAccessMemory: Int, val internalMemory: Int) {
    constructor() : this(randomAccessMemory = 0, internalMemory = 0)
}
