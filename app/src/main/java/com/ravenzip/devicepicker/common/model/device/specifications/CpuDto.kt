package com.ravenzip.devicepicker.common.model.device.specifications

data class CpuDto(
    val model: String,
    val numberOfCores: Int,
    val maxFrequency: Int,
    val configuration: String,
    val technicalProcess: String,
    val gpu: String,
) {
    constructor() :
        this(
            model = "Не определено",
            numberOfCores = 0,
            maxFrequency = 0,
            configuration = "Не определено",
            technicalProcess = "Не определено",
            gpu = "Не определено",
        )

    companion object {
        fun CpuDto.toMap(): Map<String, String> {
            return mapOf(
                "Модель" to this.model,
                "Количество ядер" to this.numberOfCores.toString(),
                "Максимальная частота" to "${this.maxFrequency} Ггц",
                "Конфигурация" to this.configuration,
                "Техпроцесс" to this.technicalProcess,
                "Графический ускоритель" to this.gpu,
            )
        }
    }
}
