package com.ravenzip.devicepicker.common.model.device.compact

import com.ravenzip.devicepicker.common.enums.TagsEnum
import com.ravenzip.devicepicker.common.model.device.price.PriceDto.Companion.convertToPrice

data class DeviceCompactDto(val info: DeviceCompactInfoDto, val tags: List<TagsEnum>) {
    constructor() : this(info = DeviceCompactInfoDto(), tags = listOf())

    fun convertToDeviceCompact(imageUrl: String = ""): DeviceCompact {
        return DeviceCompact(
            uid = this.info.uid,
            type = this.info.type,
            model = this.info.model,
            diagonal = this.info.diagonal,
            cpu = this.info.cpu,
            battery = this.info.battery,
            camera = this.info.camera,
            price = this.info.price.convertToPrice(),
            rating = this.info.rating,
            reviewsCount = this.info.reviewsCount,
            brand = this.info.model.split(' ')[0],
            tags = this.tags,
            imageUrl = imageUrl,
        )
    }
}
