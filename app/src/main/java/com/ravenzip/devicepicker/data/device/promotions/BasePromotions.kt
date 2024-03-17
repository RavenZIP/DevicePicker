package com.ravenzip.devicepicker.data.device.promotions

import com.ravenzip.devicepicker.data.device.compact.BaseDeviceCompact

interface BasePromotions : BaseDeviceCompact {
    override val id: Int
    override val type: String
    override val model: String
    override val price: Int
    override val rating: Double
    override val reviewsCount: Int
    val category: PromotionsCategory
}
