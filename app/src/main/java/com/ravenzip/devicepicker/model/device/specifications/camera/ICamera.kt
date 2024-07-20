package com.ravenzip.devicepicker.model.device.specifications.camera

interface ICamera {
    val count: Int
    val megapixels: List<Int>
    val sensors: List<String>
    val apertures: List<Double>
    val autofocus: Boolean
    val videoResolution: String
    val videoFeatures: String
}
