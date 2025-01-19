package com.ravenzip.devicepicker.model.device.compact

import com.ravenzip.devicepicker.constants.enums.TagsEnum
import com.ravenzip.devicepicker.constants.map.tagIconMap
import com.ravenzip.devicepicker.constants.map.tagsColorMap
import com.ravenzip.devicepicker.model.device.price.Price
import com.ravenzip.workshop.data.icon.Icon
import com.ravenzip.workshop.data.icon.IconConfig
import com.ravenzip.workshop.data.icon.IconWithConfig

/** Компактная модель устройства */
data class DeviceCompact(
    override val uid: String,
    override val type: String,
    override val model: String,
    override val diagonal: Double,
    override val cpu: String,
    override val battery: Int,
    override val camera: Int,
    override val price: Price,
    override val rating: Double,
    override val reviewsCount: Int,
    val brand: String,
    val tags: List<TagsEnum>,
    val imageUrl: String,
) : IDeviceCompact {
    constructor() :
        this(
            uid = "",
            type = "",
            model = "",
            diagonal = 0.0,
            cpu = "",
            battery = 0,
            camera = 0,
            price = Price(),
            rating = 0.0,
            reviewsCount = 0,
            brand = "",
            tags = listOf(),
            imageUrl = "",
        )

    companion object {
        fun DeviceCompact.convertToDeviceCompactExtended() =
            DeviceCompactExtended(
                uid = this.uid,
                type = this.type,
                model = this.model,
                diagonal = this.diagonal,
                cpu = this.cpu,
                battery = this.battery,
                camera = this.camera,
                price = this.price,
                rating = this.rating,
                reviewsCount = this.reviewsCount,
                brand = this.brand,
                tags = this.createTags(),
                imageUrl = this.imageUrl,
            )

        private fun DeviceCompact.createTags() =
            this.tags.map { tag ->
                IconWithConfig(
                    icon = Icon.ResourceIcon(tagIconMap[tag]!!),
                    config = IconConfig(size = 16, color = tagsColorMap[tag]),
                )
            }
    }
}
