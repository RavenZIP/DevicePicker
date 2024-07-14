package com.ravenzip.devicepicker.model.device.specifications

data class OperationSystem(
    val typeOfOperationSystem: String,
    val operationSystemVersion: Int,
    val shellOfTheOperationSystem: String,
    val shellVersion: Int,
    val supportGoogleServices: Boolean
) {
    constructor() :
        this(
            typeOfOperationSystem = "Не определено",
            operationSystemVersion = 0,
            shellOfTheOperationSystem = "Не определено",
            shellVersion = 0,
            supportGoogleServices = true)

    companion object {
        fun OperationSystem.toMap(): Map<String, String> {
            return mapOf(
                "Операционная система" to this.typeOfOperationSystem,
                "Версия ОС" to this.operationSystemVersion(),
                "Оболочка" to this.shellOfTheOperationSystem(),
                "Поддержка Google Services" to supportGoogleServices())
        }

        fun OperationSystem.operationSystemVersion(): String {
            return "${this.typeOfOperationSystem} ${this.operationSystemVersion}"
        }

        fun OperationSystem.shellOfTheOperationSystem(): String {
            return "${this.shellOfTheOperationSystem} ${this.shellVersion}"
        }

        fun OperationSystem.supportGoogleServices(): String {
            return if (this.supportGoogleServices) "Есть" else "Отсутствует"
        }
    }
}
