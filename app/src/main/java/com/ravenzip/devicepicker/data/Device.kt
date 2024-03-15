package com.ravenzip.devicepicker.data

data class Device(
    val type: String,
    val model: String,
    val price: Int,
    val rating: Double,
    val reviewsCount: Int
) {
    constructor() : this(type = "", model = "", price = 0, rating = 0.0, reviewsCount = 0)
}
