package com.ravenzip.devicepicker.data.device.promotions

// TODO: завести enum для type (0 - маленькие карточки, 1 - большие)
class PromotionsCategory(val name: String, val type: Int, val position: Int) {
    constructor() : this(name = "", type = 0, position = 0)
}
