package com.ravenzip.devicepicker.data.device.compact

import androidx.compose.ui.graphics.ImageBitmap
import com.ravenzip.devicepicker.data.Tag

/** Компактная модель устройства */
data class DeviceCompact(
    override val uid: String,
    override val type: String,
    override val model: String,
    override val price: Int,
    override val rating: Double,
    override val reviewsCount: Int,
    val brand: String,
    val tags: Tag,
    val image: ImageBitmap
) : IDeviceCompact {
    constructor() :
        this(
            uid = "",
            type = "",
            model = "",
            price = 0,
            rating = 0.0,
            reviewsCount = 0,
            brand = "",
            tags = Tag(),
            image = ImageBitmap(100, 200)
        )
}
