package com.ravenzip.devicepicker.common.model.device.specifications

data class BaseInfoDto(
    val type: String,
    val model: String,
    val country: String,
    val year: Int,
    val warranty: String,
) {
    constructor() : this(type = "", model = "", country = "", year = 0, warranty = "")

    companion object {
        fun BaseInfoDto.toMap(): Map<String, String> {
            return mapOf(
                "Тип" to this.type,
                "Модель" to this.model,
                "Год выпуска" to this.year.toString(),
                "Страна- производитель" to this.country,
                "Гарантия" to this.warranty,
            )
        }
    }
}
