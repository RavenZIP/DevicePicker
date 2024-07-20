package com.ravenzip.devicepicker.model.device.specifications.camera

data class FrontCamera(
    override val count: Int,
    override val megapixels: List<Int>,
    override val sensors: List<String>,
    override val apertures: List<Double>,
    override val autofocus: Boolean,
    override val videoResolution: String,
    override val videoFeatures: String,
    val flash: Boolean
) : ICamera {
    constructor() :
        this(
            count = 0,
            megapixels = emptyList(),
            sensors = emptyList(),
            apertures = emptyList(),
            autofocus = false,
            videoResolution = "Не определено",
            videoFeatures = "Не определено",
            flash = false)

    companion object {
        fun FrontCamera.toMap(): Map<String, String> {
            return mapOf(
                "Количество камер" to this.count.toString(),
                "Количество мегапикселей" to "${this.megapixels.joinToString(separator = "+")} Мп",
                "Модели сенсоров" to this.sensors.joinToString(),
                "Апертура камер" to this.apertures.joinToString(),
                "Автофокусировка камеры" to if (this.autofocus) "Есть" else "Отсутствует",
                "Вспышка" to if (this.flash) "Есть" else "Отсутствует",
                "Разрешение видео и частота кадров" to this.videoResolution,
                "Особенности и функции видеосъемки" to this.videoFeatures,
            )
        }
    }
}
