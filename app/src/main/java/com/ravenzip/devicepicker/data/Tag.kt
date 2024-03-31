package com.ravenzip.devicepicker.data

class Tag(val popular: Boolean, val lowPrice: Boolean, val highPerformance: Boolean) {
    constructor() : this(popular = false, lowPrice = false, highPerformance = false)
}
