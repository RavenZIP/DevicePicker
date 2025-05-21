package com.ravenzip.devicepicker.common.model.device.compact

/** Интерфейс, описывающий общую компактную модель устройства */
interface IDeviceCompact {
    val uid: String
    val type: String
    val model: String
    val diagonal: Double
    val cpu: String
    val battery: Int
    val camera: Int
    val rating: Double
    val reviewsCount: Int
}
