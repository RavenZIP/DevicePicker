package com.ravenzip.devicepicker.common.model.device.specifications

data class CommunicationDto(
    val bluetoothVersion: Double,
    val wifi: List<Int>,
    val nfc: Boolean,
    val navigationSystems: String,
    val irPort: Boolean,
    val otherTechnologies: String,
) {
    constructor() :
        this(
            bluetoothVersion = 0.0,
            wifi = listOf(),
            nfc = false,
            navigationSystems = "",
            irPort = false,
            otherTechnologies = "",
        )

    companion object {
        fun CommunicationDto.toMap(): Map<String, String> {
            return mapOf(
                "Bluetooth" to this.bluetoothVersion.toString(),
                "Wi-Fi" to this.wifi.joinToString(),
                "NFC" to if (this.nfc) "Есть" else "Отсутствует",
                "Системы навигации" to this.navigationSystems,
                "ИК-порт" to if (this.irPort) "Есть" else "Отсутствует",
                "Прочие технологии" to this.otherTechnologies,
            )
        }
    }
}
