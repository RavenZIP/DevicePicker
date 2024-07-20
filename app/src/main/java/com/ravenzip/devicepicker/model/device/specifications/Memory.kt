package com.ravenzip.devicepicker.model.device.specifications

data class Memory(
    val ramType: String,
    val virtualRamExtension: Int,
    val romType: String,
    val amountOfRom: Int,
    val memoryCardSlot: Boolean
) {
    constructor() :
        this(
            ramType = "Не определено",
            virtualRamExtension = 0,
            romType = "Не определено",
            amountOfRom = 0,
            memoryCardSlot = false)

    companion object {
        fun Memory.toMap(): Map<String, String> {
            return mapOf(
                "Тип оперативной памяти" to this.ramType,
                "Виртуальное расширение ОЗУ" to
                    if (this.virtualRamExtension == 0) "Отсутствует"
                    else "${this.virtualRamExtension} Гб",
                "Тип встроенной памяти" to this.romType,
                "Объем встроенной памяти" to "${this.amountOfRom} Гб",
                "Слот для карты памяти" to if (this.memoryCardSlot) "Есть" else "Отсутствует")
        }
    }
}
