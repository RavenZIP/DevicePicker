package com.ravenzip.devicepicker.model.device.specifications

data class Connectors(
    val chargingInterface: String,
    val headphoneJack: String,
    val otgSupport: Boolean
) {

    constructor() : this(chargingInterface = "", headphoneJack = "", otgSupport = false)

    companion object {
        fun Connectors.toMap(): Map<String, String> {
            return mapOf(
                "Интерфейс для зарядки" to this.chargingInterface,
                "Разъем для наушников" to
                    if (this.headphoneJack != "") this.headphoneJack else "Отсутствует",
                "Поддержка OTG" to if (this.otgSupport) "Есть" else "Отсутствует")
        }
    }
}
