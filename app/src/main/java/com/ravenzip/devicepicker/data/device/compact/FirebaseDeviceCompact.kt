package com.ravenzip.devicepicker.data.device.compact

import androidx.compose.ui.graphics.ImageBitmap
import com.ravenzip.devicepicker.data.device.FirebaseImage

/** Компактная модель устройства (Firebase Realtime Database) */
data class FirebaseDeviceCompact(
    override val id: Int,
    override val type: String,
    override val model: String,
    override val price: Int,
    override val rating: Double,
    override val reviewsCount: Int,
    val imageData: FirebaseImage
) : BaseDeviceCompact {
    constructor() :
        this(
            id = 0,
            type = "",
            model = "",
            price = 0,
            rating = 0.0,
            reviewsCount = 0,
            imageData = FirebaseImage()
        )

    fun convertToDeviceCompact(image: ImageBitmap): DeviceCompact {
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
