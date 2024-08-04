package com.ravenzip.devicepicker.state

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.ravenzip.devicepicker.constants.enums.ContainerTypeEnum
import com.ravenzip.devicepicker.model.device.compact.DeviceCompact

class DeviceCategoryState(
    val devices: SnapshotStateList<DeviceCompact>,
    val containerType: ContainerTypeEnum,
    val categoryName: String
) {
    constructor() :
        this(
            devices = mutableStateListOf(),
            containerType = ContainerTypeEnum.Default,
            categoryName = "")
}
