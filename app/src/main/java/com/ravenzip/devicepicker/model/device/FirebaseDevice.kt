package com.ravenzip.devicepicker.model.device

import com.ravenzip.devicepicker.constants.enums.TagsEnum
import com.ravenzip.devicepicker.model.Feedback
import com.ravenzip.devicepicker.model.device.compact.DeviceSpecifications
import com.ravenzip.devicepicker.model.device.configurations.PhoneConfiguration
import com.ravenzip.devicepicker.model.device.price.FirebasePrice
import com.ravenzip.devicepicker.model.device.price.FirebasePrice.Companion.convertToPrice

data class FirebaseDevice(
    val uid: String,
    val specifications: DeviceSpecifications,
    val tags: List<TagsEnum>,
    val colors: List<String>,
    val configurations: List<PhoneConfiguration>,
    val feedback: Feedback,
    val price: FirebasePrice,
) {
    constructor() :
        this(
            uid = "",
            specifications = DeviceSpecifications(),
            tags = listOf(),
            colors = listOf(),
            configurations = listOf(),
            feedback = Feedback(),
            price = FirebasePrice(),
        )

    fun convertToDevice(imageUrls: List<String> = listOf()): Device {
        return Device(
            uid = this.uid,
            specifications = this.specifications,
            colors = this.colors,
            tags = this.tags,
            configurations = this.configurations,
            feedback = this.feedback,
            price = this.price.convertToPrice(),
            imageUrls = imageUrls,
        )
    }
}
