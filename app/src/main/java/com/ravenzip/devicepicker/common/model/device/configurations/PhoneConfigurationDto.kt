package com.ravenzip.devicepicker.common.model.device.configurations

class PhoneConfigurationDto(val randomAccessMemory: Int, val internalMemory: Int) {
    constructor() : this(randomAccessMemory = 0, internalMemory = 0)
}
