package com.ravenzip.devicepicker.common.model.device.compact

class DeviceCompactInfoDto(
    override val uid: String,
    override val type: String,
    override val model: String,
    override val diagonal: Double,
    override val cpu: String,
    override val battery: Int,
    override val camera: Int,
    override val rating: Double,
    override val reviewsCount: Int,
) : IDeviceCompact {
    constructor() :
        this(
            uid = "",
            type = "",
            model = "",
            diagonal = 0.0,
            cpu = "",
            battery = 0,
            camera = 0,
            rating = 0.0,
            reviewsCount = 0,
        )
}
