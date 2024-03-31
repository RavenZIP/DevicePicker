package com.ravenzip.devicepicker.data.device.compact

class FirebaseDeviceCompactInfo(
    override val id: Int,
    override val type: String,
    override val model: String,
    override val price: Int,
    override val rating: Double,
    override val reviewsCount: Int
) : IDeviceCompact {
    constructor() : this(id = 0, type = "", model = "", price = 0, rating = 0.0, reviewsCount = 0)
}
