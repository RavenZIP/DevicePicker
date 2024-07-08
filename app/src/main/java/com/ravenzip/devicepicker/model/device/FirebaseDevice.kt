package com.ravenzip.devicepicker.model.device

import com.ravenzip.devicepicker.model.Tag
import com.ravenzip.devicepicker.model.device.compact.DeviceSpecifications

data class FirebaseDevice(
    val info: DeviceInfo,
    val specifications: DeviceSpecifications,
    val tags: Tag,
    val colors: List<String>,
    val configurations: List<PhoneConfiguration>
) {
    constructor() :
        this(
            info = DeviceInfo(),
            specifications = DeviceSpecifications(),
            tags = Tag(),
            colors = listOf(),
            configurations = listOf())

    fun convertToDevice(imageUrls: List<String> = listOf()): Device {
        return Device(
            info = this.info,
            specifications = this.specifications,
            colors = this.colors,
            tags = this.tags,
            configurations = this.configurations,
            imageUrls = imageUrls)
    }
}
