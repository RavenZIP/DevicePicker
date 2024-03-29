package com.ravenzip.devicepicker.data.device.compact

import androidx.compose.ui.graphics.ImageBitmap
import com.ravenzip.devicepicker.data.device.FirebaseImageData

data class FirebaseDeviceCompact(
    val info: FirebaseDeviceCompactInfo,
    val image: FirebaseImageData
) {
    constructor() : this(info = FirebaseDeviceCompactInfo(), image = FirebaseImageData())

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
            image = image
        )
    }
}
