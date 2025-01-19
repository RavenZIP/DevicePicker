package com.ravenzip.devicepicker.model.device.compact

import com.ravenzip.devicepicker.model.device.price.IPrice

/** Интерфейс, описывающий общую компактную модель устройства */
interface IDeviceCompact {
    val uid: String
    val type: String
    val model: String
    val diagonal: Double
    val cpu: String
    val battery: Int
    val camera: Int
    val price: IPrice
    val rating: Double
    val reviewsCount: Int
}
