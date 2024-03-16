package com.ravenzip.devicepicker.data.device.compact

import androidx.compose.ui.graphics.ImageBitmap

/** Компактная модель устройства (Firebase Realtime Database) */
data class FirebaseDeviceCompact(
    override val id: Int,
    override val type: String,
    override val model: String,
    override val price: Int,
    override val rating: Double,
    override val reviewsCount: Int,
    val imageExtension: String
) : BaseDeviceCompact {
    constructor() :
        this(
            id = 0,
            type = "",
            model = "",
            price = 0,
            rating = 0.0,
            reviewsCount = 0,
            imageExtension = ""
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
