package com.ravenzip.devicepicker.model.device.compact

/** Интерфейс, описывающий общую компактную модель устройства */
interface IDeviceCompact {
    val uid: String
    val type: String
    val model: String
    val price: Int
    val rating: Double
    val reviewsCount: Int
}
