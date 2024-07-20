package com.ravenzip.devicepicker.model.device.specifications

data class Audio(val stereoSpeakers: Boolean, val fmRadio: Boolean) {
    constructor() : this(stereoSpeakers = false, fmRadio = false)

    companion object {
        fun Audio.toMap(): Map<String, String> {
            return mapOf(
                "Стереодинамики" to if (this.stereoSpeakers) "Есть" else "Отсутствует",
                "FM-радио" to if (this.fmRadio) "Есть" else "Отсутствует")
        }
    }
}
