package com.ravenzip.devicepicker.data.device

/** Полная модель устройства */
data class Device(
    val id: Int,
    val type: String,
    val model: String,
    val price: Int,
    val rating: Double,
    val reviewsCount: Int
) {
    constructor() : this(id = 0, type = "", model = "", price = 0, rating = 0.0, reviewsCount = 0)
}
