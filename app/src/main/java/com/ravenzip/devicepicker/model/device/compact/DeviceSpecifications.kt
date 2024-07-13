package com.ravenzip.devicepicker.model.device.compact

import com.ravenzip.devicepicker.model.device.specifications.BaseInfo
import com.ravenzip.devicepicker.model.device.specifications.BaseInfo.Companion.map

class DeviceSpecifications(val baseInfo: BaseInfo, val diagonal: Int) {
    constructor() : this(baseInfo = BaseInfo(), diagonal = 0)

    companion object {
        fun DeviceSpecifications.map(): Map<String, Map<String, String>> {
            return mapOf("Общая информация" to this.baseInfo.map())

            // TODO добавить остальные характеристики
            // "Экран", "Корпус и защита", "Процессор", "Память", "Камера", "Аудио", "Сеть",
            // "Питание", "Габариты, вес", "Дополнительная информация"
        }
    }
}
