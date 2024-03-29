package com.ravenzip.devicepicker.data.device

class FirebaseImageData(val name: String, val size: Long, val extension: String) {
    constructor() : this(name = "", size = 0, extension = "")
}
