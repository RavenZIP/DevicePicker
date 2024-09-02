package com.ravenzip.devicepicker.model.device

import androidx.compose.runtime.Composable
import com.ravenzip.devicepicker.constants.enums.TagsEnum
import com.ravenzip.devicepicker.constants.map.tagIconMap
import com.ravenzip.devicepicker.constants.map.tagsColorMap
import com.ravenzip.devicepicker.model.Feedback
import com.ravenzip.devicepicker.model.Tag
import com.ravenzip.devicepicker.model.device.compact.DeviceSpecifications
import com.ravenzip.devicepicker.model.device.configurations.PhoneConfiguration
import com.ravenzip.devicepicker.model.device.specifications.Screen.Companion.diagonal
import com.ravenzip.workshop.data.icon.Icon
import com.ravenzip.workshop.data.icon.IconConfig
import com.ravenzip.workshop.data.icon.IconWithConfig

/** Полная модель устройства */
data class Device(
    val uid: String,
    val specifications: DeviceSpecifications,
    val colors: List<String>,
    val tags: List<TagsEnum>,
    val configurations: List<PhoneConfiguration>,
    val imageUrls: List<String>,
    val feedback: Feedback,
    val price: Int,
    val oldPrice: Int,
) {
    constructor() :
        this(
            uid = "",
            specifications = DeviceSpecifications(),
            colors = listOf(),
            tags = listOf(),
            configurations = listOf(),
            imageUrls = listOf(),
            feedback = Feedback(),
            price = 0,
            oldPrice = 0,
        )

    companion object {
        fun Device.createDeviceTitle() =
            "${this.specifications.baseInfo.type} ${this.specifications.baseInfo.model}, " +
                "${this.configurations[0].randomAccessMemory}/${this.configurations[0].internalMemory}Gb " +
                "${this.specifications.screen.diagonal()} ${this.specifications.baseInfo.year} ${this.colors[0]}"

        @Composable
        fun Device.createListOfTagsWithIcons() =
            this.tags.map { tag ->
                Tag(
                    name = tag,
                    icon =
                        IconWithConfig(
                            icon = Icon.ResourceIcon(tagIconMap[tag]!!),
                            config = IconConfig(size = 20, color = tagsColorMap[tag]),
                        ),
                )
            }

        @Composable
        fun Device.createListOfTagsIcons() =
            this.tags.map { tag ->
                IconWithConfig(
                    icon = Icon.ResourceIcon(tagIconMap[tag]!!),
                    config = IconConfig(size = 20, color = tagsColorMap[tag]),
                )
            }
    }
}
