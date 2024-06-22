package com.ravenzip.devicepicker.model.device

import com.ravenzip.devicepicker.model.Tag
import com.ravenzip.devicepicker.model.device.compact.FirebaseDeviceCompactInfo

data class FirebaseDevice(val info: FirebaseDeviceCompactInfo, val tags: Tag) {
    constructor() : this(info = FirebaseDeviceCompactInfo(), tags = Tag())

    fun convertToDevice(imageUrls: List<String> = listOf()): Device {
        return Device(
            uid = this.info.uid,
            type = this.info.type,
            model = this.info.model,
            price = this.info.price,
            rating = this.info.rating,
            reviewsCount = this.info.reviewsCount,
            brand = this.info.model.split(' ')[0],
            tags = this.tags,
            imageUrls = imageUrls)
    }
}
