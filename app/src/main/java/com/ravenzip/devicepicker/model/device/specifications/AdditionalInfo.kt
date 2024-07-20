package com.ravenzip.devicepicker.model.device.specifications

import com.ravenzip.devicepicker.extensions.functions.convertToString

data class AdditionalInfo(
    val biometricProtection: List<String>,
    val sensors: List<String>,
    val headphonesIsIncluded: Boolean,
    val chargerIsIncluded: Boolean,
    val equipment: String,
    val ledNotificationIndicator: Boolean,
    val features: String
) {
    constructor() :
        this(
            biometricProtection = listOf(),
            sensors = listOf(),
            headphonesIsIncluded = false,
            chargerIsIncluded = false,
            equipment = "",
            ledNotificationIndicator = false,
            features = "")

    companion object {
        fun AdditionalInfo.toMap(): Map<String, String> {
            return mapOf(
                "Биометрическая защита" to this.biometricProtection.convertToString(),
                "Датчики" to this.sensors.convertToString(),
                "Наушники" to if (this.headphonesIsIncluded) "Есть" else "Отсутствует",
                "Зарядное устройство" to if (this.chargerIsIncluded) "Есть" else "Отсутствует",
                "Комплектация" to this.equipment,
                "LED-индикатор уведомлений" to
                    if (this.ledNotificationIndicator) "Есть" else "Отсутствует",
                "Особенности, дополнительно" to this.features)
        }
    }
}
