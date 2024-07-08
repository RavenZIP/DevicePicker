package com.ravenzip.devicepicker.model.device

import com.ravenzip.devicepicker.model.device.compact.IDeviceCompact

class DeviceInfo(
    override val uid: String,
    override val type: String,
    override val model: String,
    override val price: Int,
    override val rating: Double,
    override val reviewsCount: Int,
    val questionsCount: Int
) : IDeviceCompact {
    constructor() :
        this(
            uid = "",
            type = "",
            model = "",
            price = 0,
            rating = 0.0,
            reviewsCount = 0,
            questionsCount = 0)
}
