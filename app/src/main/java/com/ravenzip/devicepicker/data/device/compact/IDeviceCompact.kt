package com.ravenzip.devicepicker.data.device.compact

/** Интерфейс, описывающий общую компактную модель устройства */
interface IDeviceCompact {
    val id: Int
    val type: String
    val model: String
    val price: Int
    val rating: Double
    val reviewsCount: Int
}
