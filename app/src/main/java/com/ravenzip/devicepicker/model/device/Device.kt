package com.ravenzip.devicepicker.model.device

import com.ravenzip.devicepicker.model.Tag
import com.ravenzip.devicepicker.model.device.compact.DeviceSpecifications

/** Полная модель устройства */
data class Device(
    val info: DeviceInfo,
    val specifications: DeviceSpecifications,
    val colors: List<String>,
    val tags: Tag,
    val configurations: List<PhoneConfiguration>,
    val imageUrls: List<String>
) {
    constructor() :
        this(
            info = DeviceInfo(),
            specifications = DeviceSpecifications(),
            colors = listOf(),
            tags = Tag(),
            configurations = listOf(),
            imageUrls = listOf())
}
