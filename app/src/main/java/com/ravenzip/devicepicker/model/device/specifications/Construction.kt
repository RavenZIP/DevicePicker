package com.ravenzip.devicepicker.model.device.specifications

data class Construction(
    val material: String,
    val security: String,
    val protectiveScreenCoating: String,
    val typeOfCutoutOnScreen: String
) {
    constructor() :
        this(
            material = "Не определено",
            security = "Не определено",
            protectiveScreenCoating = "Не определено",
            typeOfCutoutOnScreen = "Не определено")

    companion object {
        fun Construction.toMap(): Map<String, String> {
            return mapOf(
                "Материал" to this.material,
                "Защищенность" to this.security,
                "Защитное покрытие экрана" to this.protectiveScreenCoating,
                "Тип выреза на экране" to this.typeOfCutoutOnScreen)
        }
    }
}
