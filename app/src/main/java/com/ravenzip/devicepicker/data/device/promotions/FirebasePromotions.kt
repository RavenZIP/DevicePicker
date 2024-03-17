package com.ravenzip.devicepicker.data.device.promotions

import androidx.compose.ui.graphics.ImageBitmap
import com.ravenzip.devicepicker.data.device.FirebaseImageData

class FirebasePromotions(
    override val id: Int,
    override val type: String,
    override val model: String,
    override val price: Int,
    override val rating: Double,
    override val reviewsCount: Int,
    override val category: PromotionsCategory,
    val imageData: FirebaseImageData,
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
            imageData = FirebaseImageData(),
        )

    fun convertToPromotions(image: ImageBitmap): Promotions {
        return Promotions(
            id = this.id,
            type = this.type,
            model = this.model,
            price = this.price,
            rating = this.rating,
            reviewsCount = this.reviewsCount,
            category = this.category,
            image = image,
        )
    }
}
