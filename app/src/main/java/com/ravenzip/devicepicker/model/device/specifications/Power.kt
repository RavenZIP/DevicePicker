package com.ravenzip.devicepicker.model.device.specifications

data class Power(
    val type: String,
    val capacity: Int,
    val powerSupplyVoltageOfTheCharger: String,
    val chargerOutputPower: Int,
    val fastCharge: Boolean,
    val typesOfFastCharging: String,
    val supportWirelessCharging: Boolean,
    val supportWirelessReverseCharging: Boolean
) {
    constructor() :
        this(
            type = "",
            capacity = 0,
            powerSupplyVoltageOfTheCharger = "",
            chargerOutputPower = 0,
            fastCharge = false,
            typesOfFastCharging = "",
            supportWirelessCharging = false,
            supportWirelessReverseCharging = false)

    companion object {
        fun Power.toMap(): Map<String, String> {
            return mapOf(
                "Тип аккумулятора" to this.type,
                "Емкость аккумулятора" to "${this.capacity} мА*ч",
                "Напряжение питания ЗУ" to this.powerSupplyVoltageOfTheCharger,
                "Выходная мощность ЗУ" to "${this.chargerOutputPower} Вт",
                "Быстрая зарядка" to if (this.fastCharge) "Есть" else "Отсутствует",
                "Стандарты быстрой зарядки" to this.typesOfFastCharging,
                "Поддержка беспроводной зарядки" to
                    if (this.supportWirelessCharging) "Есть" else "Отсутствует",
                "Поддержка беспроводной реверсивной зарядки" to
                    if (this.supportWirelessReverseCharging) "Есть" else "Отсутствует")
        }
    }
}
