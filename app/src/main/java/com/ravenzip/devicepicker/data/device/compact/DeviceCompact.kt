package com.ravenzip.devicepicker.data.device.compact

import androidx.compose.ui.graphics.ImageBitmap

/** Компактная модель устройства */
data class DeviceCompact(
    override val id: Int,
    override val type: String,
    override val model: String,
    override val price: Int,
    override val rating: Double,
    override val reviewsCount: Int,
    val image: ImageBitmap
) : BaseDeviceCompact {
    constructor() :
        this(
            id = 0,
            type = "",
            model = "",
            price = 0,
            rating = 0.0,
            reviewsCount = 0,
            image = ImageBitmap(100, 200)
        )
}
