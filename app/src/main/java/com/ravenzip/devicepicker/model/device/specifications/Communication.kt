package com.ravenzip.devicepicker.model.device.specifications

import com.ravenzip.devicepicker.extensions.functions.convertToString

data class Communication(
    val bluetoothVersion: Double,
    val wifi: List<Int>,
    val nfc: Boolean,
    val navigationSystems: String,
    val irPort: Boolean,
    val otherTechnologies: String
) {
    constructor() :
        this(
            bluetoothVersion = 0.0,
            wifi = listOf(),
            nfc = false,
            navigationSystems = "",
            irPort = false,
            otherTechnologies = "")

    companion object {
        fun Communication.toMap(): Map<String, String> {
            return mapOf(
                "Bluetooth" to this.bluetoothVersion.toString(),
                "Wi-Fi" to this.wifi.convertToString(),
                "NFC" to if (this.nfc) "Есть" else "Отсутствует",
                "Системы навигации" to this.navigationSystems,
                "ИК-порт" to if (this.irPort) "Есть" else "Отсутствует",
                "Прочие технологии" to this.otherTechnologies)
        }
    }
}
