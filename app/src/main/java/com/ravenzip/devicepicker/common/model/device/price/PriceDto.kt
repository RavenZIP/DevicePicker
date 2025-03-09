package com.ravenzip.devicepicker.common.model.device.price

class PriceDto(override val old: Int, override val current: Int) : IPrice {
    constructor() : this(old = 0, current = 0)

    companion object {
        fun PriceDto.convertToPrice() =
            Price(
                this.old,
                this.current,
                Price.formatPrice(this.old),
                Price.formatPrice(this.current),
            )
    }
}
