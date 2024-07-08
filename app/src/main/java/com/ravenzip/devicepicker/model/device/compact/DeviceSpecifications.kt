package com.ravenzip.devicepicker.model.device.compact

class DeviceSpecifications(
    val diagonal: Int,
    val year: Int,
) {
    constructor() : this(diagonal = 0, year = 0)
}
