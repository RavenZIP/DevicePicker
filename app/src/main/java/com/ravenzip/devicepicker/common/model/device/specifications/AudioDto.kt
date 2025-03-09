package com.ravenzip.devicepicker.common.model.device.specifications

data class AudioDto(val stereoSpeakers: Boolean, val fmRadio: Boolean) {
    constructor() : this(stereoSpeakers = false, fmRadio = false)

    companion object {
        fun AudioDto.toMap(): Map<String, String> {
            return mapOf(
                "Стереодинамики" to if (this.stereoSpeakers) "Есть" else "Отсутствует",
                "FM-радио" to if (this.fmRadio) "Есть" else "Отсутствует",
            )
        }
    }
}
