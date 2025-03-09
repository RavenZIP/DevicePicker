package com.ravenzip.devicepicker.common.model.device

import com.ravenzip.devicepicker.common.enums.TagsEnum
import com.ravenzip.devicepicker.common.model.FeedbackDto
import com.ravenzip.devicepicker.common.model.device.compact.DeviceSpecifications
import com.ravenzip.devicepicker.common.model.device.configurations.PhoneConfigurationDto
import com.ravenzip.devicepicker.common.model.device.price.PriceDto
import com.ravenzip.devicepicker.common.model.device.price.PriceDto.Companion.convertToPrice

data class DeviceDto(
    val uid: String,
    val specifications: DeviceSpecifications,
    val tags: List<TagsEnum>,
    val colors: List<String>,
    val configurations: List<PhoneConfigurationDto>,
    val feedback: FeedbackDto,
    val price: PriceDto,
) {
    constructor() :
        this(
            uid = "",
            specifications = DeviceSpecifications(),
            tags = listOf(),
            colors = listOf(),
            configurations = listOf(),
            feedback = FeedbackDto(),
            price = PriceDto(),
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
