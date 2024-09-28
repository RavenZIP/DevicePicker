package com.ravenzip.devicepicker.model.device.price

import java.text.DecimalFormat

class Price(
    override val old: Int,
    override val current: Int,
    val oldFormatted: String,
    val currentFormatted: String,
) : IPrice {
    constructor() : this(0, 0, "", "")

    companion object {
        private val pattern = DecimalFormat("###,###,###")

        fun formatPrice(price: Int) = pattern.format(price).replace(",", " ") + " ₽"
    }
}
