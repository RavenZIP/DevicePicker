package com.ravenzip.devicepicker.model.device

import com.ravenzip.devicepicker.model.Feedback
import com.ravenzip.devicepicker.model.Tags
import com.ravenzip.devicepicker.model.device.compact.DeviceSpecifications
import com.ravenzip.devicepicker.model.device.configurations.PhoneConfiguration

data class FirebaseDevice(
    val uid: String,
    val specifications: DeviceSpecifications,
    val tags: Tags,
    val colors: List<String>,
    val configurations: List<PhoneConfiguration>,
    val feedback: Feedback,
    val price: Int
) {
    constructor() :
        this(
            uid = "",
            specifications = DeviceSpecifications(),
            tags = Tags(),
            colors = listOf(),
            configurations = listOf(),
            feedback = Feedback(),
            price = 0)

    fun convertToDevice(imageUrls: List<String> = listOf()): Device {
        return Device(
            uid = this.uid,
            specifications = this.specifications,
            colors = this.colors,
            tags = this.tags,
            configurations = this.configurations,
            feedback = this.feedback,
            price = this.price,
            imageUrls = imageUrls)
    }
}
