package com.ravenzip.devicepicker.common.model.device.compact

/** Интерфейс, описывающий общую компактную модель устройства */
interface IDeviceCompact {
    val uid: String
    val type: String
    val model: String
    @Deprecated("Не актуально, убрать") val diagonal: Double
    @Deprecated("Не актуально, убрать") val cpu: String
    @Deprecated("Не актуально, убрать") val battery: Int
    @Deprecated("Не актуально, убрать") val camera: Int
    val rating: Double
    val reviewsCount: Int
}
