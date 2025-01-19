package com.ravenzip.devicepicker.model.device.compact

import com.ravenzip.devicepicker.model.device.price.FirebasePrice

class FirebaseDeviceCompactInfo(
    override val uid: String,
    override val type: String,
    override val model: String,
    override val diagonal: Double,
    override val cpu: String,
    override val battery: Int,
    override val camera: Int,
    override val price: FirebasePrice,
    override val rating: Double,
    override val reviewsCount: Int,
) : IDeviceCompact {
    constructor() :
        this(
            uid = "",
            type = "",
            model = "",
            diagonal = 0.0,
            cpu = "",
            battery = 0,
            camera = 0,
            price = FirebasePrice(),
            rating = 0.0,
            reviewsCount = 0,
        )
}
