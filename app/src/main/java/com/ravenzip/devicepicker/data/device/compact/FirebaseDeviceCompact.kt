package com.ravenzip.devicepicker.data.device.compact

import androidx.compose.ui.graphics.ImageBitmap
import com.ravenzip.devicepicker.data.Tag
import com.ravenzip.devicepicker.data.device.FirebaseImageData

data class FirebaseDeviceCompact(
    val info: FirebaseDeviceCompactInfo,
    val image: FirebaseImageData,
    val tags: Tag
) {
    constructor() :
        this(info = FirebaseDeviceCompactInfo(), image = FirebaseImageData(), tags = Tag())

    fun convertToDeviceCompact(
        image: ImageBitmap = ImageBitmap(width = 200, height = 100)
    ): DeviceCompact {
        return DeviceCompact(
            id = this.info.id,
            type = this.info.type,
            model = this.info.model,
            price = this.info.price,
            rating = this.info.rating,
            reviewsCount = this.info.reviewsCount,
            tags = this.tags,
            image = image
        )
    }
}
