package com.ravenzip.devicepicker.model.device.compact

class FirebaseDeviceCompactInfo(
    override val uid: String,
    override val type: String,
    override val model: String,
    override val price: Int,
    override val rating: Double,
    override val reviewsCount: Int
) : IDeviceCompact {
    constructor() : this(uid = "", type = "", model = "", price = 0, rating = 0.0, reviewsCount = 0)
}
