package com.ravenzip.devicepicker.model.device.specifications

data class Dimensions(
    val width: Double,
    val height: Double,
    val thickness: Double,
    val weight: Int
) {
    constructor() : this(width = 0.0, height = 0.0, thickness = 0.0, weight = 0)

    companion object {
        fun Dimensions.toMap(): Map<String, String> {
            return mapOf(
                "Ширина" to "${this.width} мм",
                "Высота" to "${this.height} мм",
                "Толщина" to "${this.thickness} мм",
                "Вес" to "${this.weight} г")
        }
    }
}
