package com.ravenzip.devicepicker.model.device.compact

import com.ravenzip.devicepicker.model.device.specifications.BaseInfo
import com.ravenzip.devicepicker.model.device.specifications.BaseInfo.Companion.toMap
import com.ravenzip.devicepicker.model.device.specifications.Construction
import com.ravenzip.devicepicker.model.device.specifications.Construction.Companion.toMap
import com.ravenzip.devicepicker.model.device.specifications.Screen
import com.ravenzip.devicepicker.model.device.specifications.Screen.Companion.toMap

class DeviceSpecifications(
    val baseInfo: BaseInfo,
    val screen: Screen,
    val construction: Construction
) {
    constructor() : this(baseInfo = BaseInfo(), screen = Screen(), construction = Construction())

    companion object {
        fun DeviceSpecifications.toMap(): Map<String, Map<String, String>> {
            return mapOf(
                "Общая информация" to this.baseInfo.toMap(),
                "Экран" to this.screen.toMap(),
                "Корпус и защита" to this.construction.toMap())

            // TODO добавить остальные характеристики
            // "Процессор", "Память", "Камера", "Аудио", "Сеть", "Питание", "Габариты, вес",
            // "Дополнительная информация"
        }
    }
}
