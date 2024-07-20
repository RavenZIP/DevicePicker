package com.ravenzip.devicepicker.model.device.compact

import com.ravenzip.devicepicker.model.device.specifications.BaseInfo
import com.ravenzip.devicepicker.model.device.specifications.BaseInfo.Companion.toMap
import com.ravenzip.devicepicker.model.device.specifications.Construction
import com.ravenzip.devicepicker.model.device.specifications.Construction.Companion.toMap
import com.ravenzip.devicepicker.model.device.specifications.Cpu
import com.ravenzip.devicepicker.model.device.specifications.Cpu.Companion.toMap
import com.ravenzip.devicepicker.model.device.specifications.Memory
import com.ravenzip.devicepicker.model.device.specifications.Memory.Companion.toMap
import com.ravenzip.devicepicker.model.device.specifications.OperationSystem
import com.ravenzip.devicepicker.model.device.specifications.OperationSystem.Companion.toMap
import com.ravenzip.devicepicker.model.device.specifications.Screen
import com.ravenzip.devicepicker.model.device.specifications.Screen.Companion.toMap

class DeviceSpecifications(
    val baseInfo: BaseInfo,
    val screen: Screen,
    val construction: Construction,
    val operationSystem: OperationSystem,
    val cpu: Cpu,
    val memory: Memory
) {
    constructor() :
        this(
            baseInfo = BaseInfo(),
            screen = Screen(),
            construction = Construction(),
            operationSystem = OperationSystem(),
            cpu = Cpu(),
            memory = Memory())

    companion object {
        fun DeviceSpecifications.toMap(): Map<String, Map<String, String>> {
            return mapOf(
                "Общая информация" to this.baseInfo.toMap(),
                "Экран" to this.screen.toMap(),
                "Корпус и защита" to this.construction.toMap(),
                "Операционная система" to this.operationSystem.toMap(),
                "Процессор" to this.cpu.toMap(),
                "Память" to this.memory.toMap())

            // TODO добавить остальные характеристики
            // "Камера", "Аудио", "Сеть", "Питание", "Габариты, вес",
            // "Дополнительная информация"
        }
    }
}
