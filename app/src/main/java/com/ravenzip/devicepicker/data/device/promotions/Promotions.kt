package com.ravenzip.devicepicker.data.device.promotions

import androidx.compose.ui.graphics.ImageBitmap

class Promotions(
    override val id: Int,
    override val type: String,
    override val model: String,
    override val price: Int,
    override val rating: Double,
    override val reviewsCount: Int,
    override val category: PromotionsCategory,
    val image: ImageBitmap,
) : BasePromotions {
    constructor() :
        this(
            id = 0,
            type = "",
            model = "",
            price = 0,
            rating = 0.0,
            reviewsCount = 0,
            category = PromotionsCategory(),
            image = ImageBitmap(100, 200),
        )
}
