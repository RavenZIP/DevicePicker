package com.ravenzip.devicepicker.model.device.specifications

data class Screen(
    val diagonal: Int,
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
    val sensorSamplingRate: Int
) {
    constructor() :
        this(
            diagonal = 0,
            width = 0,
            height = 0,
            typeOfMatrix = "",
            brightness = 0,
            screenRefreshRate = 0,
            pixelDensity = 0,
            numberOfColors = 0,
            verticalRatio = 0,
            horizontalRatio = 0,
            sensorSamplingRate = 0,
            features = "")

    companion object {
        fun Screen.toMap(): Map<String, String> {
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
                "Особенности" to this.features)
        }

        fun Screen.resolution(): String {
            return "${this.width}x${this.height}"
        }

        fun Screen.aspectRatio(): String {
            return "${this.verticalRatio}:${this.horizontalRatio}"
        }

        fun Screen.diagonal(): String {
            return "${this.diagonal}\""
        }

        fun Screen.brightness(): String {
            return "${this.brightness} Кд/м²"
        }

        fun Screen.refreshRate(isScreen: Boolean = true): String {
            return "${ if (isScreen) this.screenRefreshRate else this.sensorSamplingRate} Гц"
        }

        fun Screen.pixelDensity(): String {
            return "${this.pixelDensity} ppi"
        }

        fun Screen.numberOfColors(): String {
            return "${this.numberOfColors} млрд."
        }
    }
}
