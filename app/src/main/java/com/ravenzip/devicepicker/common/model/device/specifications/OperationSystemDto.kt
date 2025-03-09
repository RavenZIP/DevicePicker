package com.ravenzip.devicepicker.common.model.device.specifications

data class OperationSystemDto(
    val typeOfOperationSystem: String,
    val operationSystemVersion: Int,
    val shellOfTheOperationSystem: String,
    val shellVersion: Int,
    val supportGoogleServices: Boolean,
) {
    constructor() :
        this(
            typeOfOperationSystem = "Не определено",
            operationSystemVersion = 0,
            shellOfTheOperationSystem = "Не определено",
            shellVersion = 0,
            supportGoogleServices = true,
        )

    companion object {
        fun OperationSystemDto.toMap(): Map<String, String> {
            return mapOf(
                "Операционная система" to this.typeOfOperationSystem,
                "Версия ОС" to this.operationSystemVersion(),
                "Оболочка" to this.shellOfTheOperationSystem(),
                "Поддержка Google Services" to supportGoogleServices(),
            )
        }

        fun OperationSystemDto.operationSystemVersion(): String {
            return "${this.typeOfOperationSystem} ${this.operationSystemVersion}"
        }

        fun OperationSystemDto.shellOfTheOperationSystem(): String {
            return "${this.shellOfTheOperationSystem} ${this.shellVersion}"
        }

        fun OperationSystemDto.supportGoogleServices(): String {
            return if (this.supportGoogleServices) "Есть" else "Отсутствует"
        }
    }
}
