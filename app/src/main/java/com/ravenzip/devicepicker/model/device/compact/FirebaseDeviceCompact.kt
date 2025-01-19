package com.ravenzip.devicepicker.model.device.compact

import com.ravenzip.devicepicker.constants.enums.TagsEnum
import com.ravenzip.devicepicker.model.device.price.FirebasePrice.Companion.convertToPrice

data class FirebaseDeviceCompact(val info: FirebaseDeviceCompactInfo, val tags: List<TagsEnum>) {
    constructor() : this(info = FirebaseDeviceCompactInfo(), tags = listOf())

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
