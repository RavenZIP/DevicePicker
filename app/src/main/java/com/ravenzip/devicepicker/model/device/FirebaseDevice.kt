package com.ravenzip.devicepicker.model.device

import com.ravenzip.devicepicker.model.Tag
import com.ravenzip.devicepicker.model.device.compact.FirebaseDeviceInfo

data class FirebaseDevice(
    val info: FirebaseDeviceInfo,
    val tags: Tag,
    val configurations: List<PhoneConfiguration>
) {
    constructor() : this(info = FirebaseDeviceInfo(), tags = Tag(), configurations = listOf())

    fun convertToDevice(imageUrls: List<String> = listOf()): Device {
        return Device(
            uid = this.info.uid,
            type = this.info.type,
            model = this.info.model,
            price = this.info.price,
            rating = this.info.rating,
            reviewsCount = this.info.reviewsCount,
            diagonal = this.info.diagonal,
            year = this.info.year,
            randomAccessMemory = this.info.randomAccessMemory,
            internalMemory = this.info.internalMemory,
            colors = this.info.colors,
            brand = this.info.model.split(' ')[0],
            tags = this.tags,
            configurations = this.configurations,
            imageUrls = imageUrls)
    }
}
