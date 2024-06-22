package com.ravenzip.devicepicker.model

class Tag(
    val popular: Boolean,
    val lowPrice: Boolean,
    val highPerformance: Boolean,
    val theBest: Boolean
) {
    constructor() :
        this(popular = false, lowPrice = false, highPerformance = false, theBest = false)
}
