package com.ravenzip.devicepicker.common.model.device.specifications

data class ScreenDto(
    val diagonal: Double,
    val width: Int,
    val height: Int,
    val typeOfMatrix: String,
    val brightness: Int,
    val screenRefreshRate: Int,
    val pixelDensity: Int,
    val numberOfColors: Int,
    val features: String,
    val verticalRatio: Int,
    val horizontalRatio: Int,
    val sensorSamplingRate: Int,
) {
    constructor() :
        this(
            diagonal = 0.0,
            width = 0,
            height = 0,
            typeOfMatrix = "Не определено",
            brightness = 0,
            screenRefreshRate = 0,
            pixelDensity = 0,
            numberOfColors = 0,
            verticalRatio = 0,
            horizontalRatio = 0,
            sensorSamplingRate = 0,
            features = "Не определено",
        )

    companion object {
        fun ScreenDto.toMap(): Map<String, String> {
            return mapOf(
                "Диагональ" to this.diagonal(),
                "Разрешение экрана" to this.resolution(),
                "Тип матрицы" to this.typeOfMatrix,
                "Яркость" to this.brightness(),
                "Частота обновления экрана" to this.refreshRate(),
                "Плотность пикселей" to this.pixelDensity(),
                "Количество цветов" to this.numberOfColors(),
                "Соотношение сторон" to this.aspectRatio(),
                "Частота дискретизации сенсора" to this.refreshRate(isScreen = false),
                "Особенности" to this.features,
            )
        }

        fun ScreenDto.resolution(): String {
            return "${this.width}x${this.height}"
        }

        fun ScreenDto.aspectRatio(): String {
            return "${this.verticalRatio}:${this.horizontalRatio}"
        }

        fun ScreenDto.diagonal(): String {
            return "${this.diagonal}\""
        }

        fun ScreenDto.brightness(): String {
            return "${this.brightness} Кд/м²"
        }

        fun ScreenDto.refreshRate(isScreen: Boolean = true): String {
            return "${ if (isScreen) this.screenRefreshRate else this.sensorSamplingRate} Гц"
        }

        fun ScreenDto.pixelDensity(): String {
            return "${this.pixelDensity} ppi"
        }

        fun ScreenDto.numberOfColors(): String {
            return "${this.numberOfColors} млрд."
        }
    }
}
