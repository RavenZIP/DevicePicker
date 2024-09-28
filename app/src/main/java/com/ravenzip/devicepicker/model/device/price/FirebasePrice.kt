package com.ravenzip.devicepicker.model.device.price

class FirebasePrice(override val old: Int, override val current: Int) : IPrice {
    constructor() : this(old = 0, current = 0)

    companion object {
        fun FirebasePrice.convertToPrice() =
            Price(
                this.old,
                this.current,
                Price.formatPrice(this.old),
                Price.formatPrice(this.current),
            )
    }
}
