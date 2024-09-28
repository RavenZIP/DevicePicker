package com.ravenzip.devicepicker.model.device.compact

import com.ravenzip.devicepicker.model.device.price.Price
import com.ravenzip.workshop.data.icon.IconWithConfig

data class DeviceCompactExtended(
    override val uid: String,
    override val type: String,
    override val model: String,
    override val diagonal: Double,
    override val price: Price,
    override val rating: Double,
    override val reviewsCount: Int,
    val brand: String,
    val tags: List<IconWithConfig>,
    val imageUrl: String,
) : IDeviceCompact {
    constructor() :
        this(
            uid = "",
            type = "",
            model = "",
            diagonal = 0.0,
            price = Price(),
            rating = 0.0,
            reviewsCount = 0,
            brand = "",
            tags = listOf(),
            imageUrl = "",
        )
}
