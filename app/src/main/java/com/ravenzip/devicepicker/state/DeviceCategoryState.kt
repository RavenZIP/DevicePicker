package com.ravenzip.devicepicker.state

import com.ravenzip.devicepicker.constants.enums.ContainerTypeEnum
import com.ravenzip.devicepicker.model.device.compact.DeviceCompact

data class DeviceCategoryState(
    val devices: List<DeviceCompact>,
    val containerType: ContainerTypeEnum,
    val categoryName: String
) {
    constructor() :
        this(devices = listOf(), containerType = ContainerTypeEnum.Default, categoryName = "")
}
