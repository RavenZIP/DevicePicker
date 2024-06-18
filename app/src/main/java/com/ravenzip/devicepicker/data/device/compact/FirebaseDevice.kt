package com.ravenzip.devicepicker.data.device.compact

import androidx.compose.ui.graphics.ImageBitmap
import com.ravenzip.devicepicker.data.Tag
import com.ravenzip.devicepicker.data.device.Device
import com.ravenzip.devicepicker.data.device.FirebaseImageData

data class FirebaseDevice(
    val info: FirebaseDeviceCompactInfo,
    val image: FirebaseImageData,
    val tags: Tag
) {
    constructor() :
        this(info = FirebaseDeviceCompactInfo(), image = FirebaseImageData(), tags = Tag())

    fun convertToDevice(image: ImageBitmap = ImageBitmap(width = 200, height = 100)): Device {
        return Device(
            uid = this.info.uid,
            type = this.info.type,
            model = this.info.model,
            price = this.info.price,
            rating = this.info.rating,
            reviewsCount = this.info.reviewsCount,
            brand = this.info.model.split(' ')[0],
            tags = this.tags,
            image = image
        )
    }
}
