package com.ravenzip.devicepicker.model.device.specifications.camera

data class BackCamera(
    override val count: Int,
    override val megapixels: List<Int>,
    override val sensors: List<String>,
    override val apertures: List<Double>,
    override val autofocus: Boolean,
    override val videoResolution: String,
    override val videoFeatures: String,
    val types: List<String>,
    val flashType: String,
    val viewingAngle: Int,
    val opticalStabilization: Boolean,
    val photographyFeatures: String,
    val photographyFunctions: String,
    val videoRecordingFormat: String,
    val slowMotionVideoRecording: Boolean,
) : ICamera {
    constructor() :
        this(
            count = 0,
            megapixels = emptyList(),
            types = emptyList(),
            sensors = emptyList(),
            apertures = emptyList(),
            autofocus = false,
            flashType = "Не определено",
            viewingAngle = 0,
            opticalStabilization = false,
            photographyFeatures = "Не определено",
            photographyFunctions = "Не определено",
            videoRecordingFormat = "Не определено",
            videoResolution = "Не определено",
            slowMotionVideoRecording = false,
            videoFeatures = "Не определено")

    companion object {
        fun BackCamera.toMap(): Map<String, String> {
            return mapOf(
                "Количество камер" to this.count.toString(),
                "Количество мегапикселей" to "${this.megapixels.joinToString(separator = "+")} Мп",
                "Типы модулей" to this.types.joinToString(),
                "Модели сенсоров" to this.sensors.joinToString(),
                "Апертура камер" to this.apertures.joinToString(),
                "Автофокусировка камеры" to if (this.autofocus) "Есть" else "Отсутствует",
                "Тип вспышки" to this.flashType,
                "Угол обзора" to this.viewingAngle.toString(),
                "Оптическая стабилизация" to
                    if (this.opticalStabilization) "Есть" else "Отсутствует",
                "Особенности и технологии" to this.photographyFeatures,
                "Режимы и функции фотосъемки" to this.photographyFunctions,
                "Формат видеосъемки" to this.videoRecordingFormat,
                "Разрешение видео и частота кадров" to this.videoResolution,
                "Замедленная видеосъемка" to
                    if (this.slowMotionVideoRecording) "Есть" else "Отсутствует",
                "Особенности и функции видеосъемки" to this.videoFeatures)
        }
    }
}
