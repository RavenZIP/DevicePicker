package com.ravenzip.devicepicker.common.model.device

import com.ravenzip.devicepicker.common.enums.TagsEnum
import com.ravenzip.devicepicker.common.model.FeedbackDto
import com.ravenzip.devicepicker.common.model.Tag
import com.ravenzip.devicepicker.common.model.device.compact.DeviceSpecifications
import com.ravenzip.devicepicker.common.model.device.configurations.PhoneConfigurationDto
import com.ravenzip.devicepicker.common.model.device.specifications.ScreenDto.Companion.diagonal
import com.ravenzip.workshop.data.icon.IconConfig
import com.ravenzip.workshop.data.icon.IconData
import com.ravenzip.workshop.data.icon.IconWithConfig

/** Полная модель устройства */
data class Device(
    val uid: String = "",
    val specifications: DeviceSpecifications = DeviceSpecifications(),
    val colors: List<String> = listOf(),
    val tags: List<TagsEnum> = listOf(),
    val configurations: List<PhoneConfigurationDto> = listOf(),
    val imageUrls: List<String> = listOf(),
    val feedback: FeedbackDto = FeedbackDto(),
) {
    companion object {
        fun Device.createDeviceTitle() =
            "${this.specifications.screen.diagonal()} ${this.specifications.baseInfo.type} " +
                "${this.specifications.baseInfo.model}, ${this.specifications.baseInfo.year}"

        fun Device.createTags() =
            this.tags.map { tag ->
                Tag(
                    tag = tag,
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
