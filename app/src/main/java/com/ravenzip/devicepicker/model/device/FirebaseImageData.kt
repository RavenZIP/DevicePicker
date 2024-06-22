package com.ravenzip.devicepicker.model.device

class FirebaseImageData(
    val brand: String,
    val name: String,
    val size: Long,
    val extension: String
) {
    constructor() : this(brand = "", name = "", size = 0, extension = "")
}
