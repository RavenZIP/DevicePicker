package com.ravenzip.devicepicker.common.model.device

import com.ravenzip.devicepicker.common.enums.TagsEnum
import com.ravenzip.devicepicker.common.model.FeedbackDto
import com.ravenzip.devicepicker.common.model.Tag
import com.ravenzip.devicepicker.common.model.device.compact.DeviceSpecifications
import com.ravenzip.devicepicker.common.model.device.configurations.PhoneConfigurationDto
import com.ravenzip.devicepicker.common.model.device.price.Price
import com.ravenzip.devicepicker.common.model.device.specifications.ScreenDto.Companion.diagonal
import com.ravenzip.workshop.data.icon.IconConfig
import com.ravenzip.workshop.data.icon.IconData
import com.ravenzip.workshop.data.icon.IconWithConfig

/** Полная модель устройства */
data class Device(
    val uid: String,
    val specifications: DeviceSpecifications,
    val colors: List<String>,
    val tags: List<TagsEnum>,
    val configurations: List<PhoneConfigurationDto>,
    val imageUrls: List<String>,
    val feedback: FeedbackDto,
    val price: Price,
) {
    constructor() :
        this(
            uid = "",
            specifications = DeviceSpecifications(),
            colors = listOf(),
            tags = listOf(),
            configurations = listOf(),
            imageUrls = listOf(),
            feedback = FeedbackDto(),
            price = Price(),
        )

    companion object {
        fun Device.createDeviceTitle() =
            "${this.specifications.baseInfo.type} ${this.specifications.baseInfo.model}, " +
                "${this.configurations[0].randomAccessMemory}/${this.configurations[0].internalMemory}Gb " +
                "${this.specifications.screen.diagonal()} ${this.specifications.baseInfo.year} ${this.colors[0]}"

        fun Device.createTags() =
            this.tags.map { tag ->
                Tag(
                    name = tag,
                    icon =
                        IconWithConfig(
                            icon = IconData.ResourceIcon(tag.icon),
                            config = IconConfig(size = 20, color = tag.color),
                        ),
                )
            }

        fun Device.createShortTags() =
            this.tags.map { tag ->
                IconWithConfig(
                    icon = IconData.ResourceIcon(tag.icon),
                    config = IconConfig(size = 20, color = tag.color),
                )
            }
    }
}
