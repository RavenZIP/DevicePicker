package com.ravenzip.devicepicker.model.device.compact

import com.ravenzip.devicepicker.model.Tag

data class FirebaseDeviceCompact(val info: FirebaseDeviceCompactInfo, val tags: Tag) {
    constructor() : this(info = FirebaseDeviceCompactInfo(), tags = Tag())

    fun convertToDeviceCompact(imageUrl: String = ""): DeviceCompact {
        return DeviceCompact(
            uid = this.info.uid,
            type = this.info.type,
            model = this.info.model,
            price = this.info.price,
            rating = this.info.rating,
            reviewsCount = this.info.reviewsCount,
            brand = this.info.model.split(' ')[0],
            tags = this.tags,
            imageUrl = imageUrl)
    }
}
