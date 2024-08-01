package com.ravenzip.devicepicker.model.device

import com.ravenzip.devicepicker.model.Feedback
import com.ravenzip.devicepicker.model.Tags
import com.ravenzip.devicepicker.model.device.compact.DeviceSpecifications
import com.ravenzip.devicepicker.model.device.configurations.PhoneConfiguration

/** Полная модель устройства */
data class Device(
    val uid: String,
    val specifications: DeviceSpecifications,
    val colors: List<String>,
    val tags: Tags,
    val configurations: List<PhoneConfiguration>,
    val imageUrls: List<String>,
    val feedback: Feedback,
    val price: Int,
) {
    constructor() :
        this(
            uid = "",
            specifications = DeviceSpecifications(),
            colors = listOf(),
            tags = Tags(),
            configurations = listOf(),
            imageUrls = listOf(),
            feedback = Feedback(),
            price = 0)
}
