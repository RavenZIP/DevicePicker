package com.ravenzip.devicepicker.data.device.compact

import androidx.compose.ui.graphics.ImageBitmap
import com.ravenzip.devicepicker.data.device.FirebaseImageData

/** Компактная модель устройства (Firebase Realtime Database) */
data class FirebaseDeviceCompact(
    override val id: Int,
    override val type: String,
    override val model: String,
    override val price: Int,
    override val rating: Double,
    override val reviewsCount: Int,
    val imageData: FirebaseImageData
) : BaseDeviceCompact {
    constructor() :
        this(
            id = 0,
            type = "",
            model = "",
            price = 0,
            rating = 0.0,
            reviewsCount = 0,
            imageData = FirebaseImageData()
        )

    fun convertToDeviceCompact(
        image: ImageBitmap = ImageBitmap(width = 200, height = 100)
    ): DeviceCompact {
        return DeviceCompact(
            id = this.id,
            type = this.type,
            model = this.model,
            price = this.price,
            rating = this.rating,
            reviewsCount = this.reviewsCount,
            image = image
        )
    }
}
