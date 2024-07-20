package com.ravenzip.devicepicker.model.device.compact

import com.ravenzip.devicepicker.model.device.specifications.AdditionalInfo
import com.ravenzip.devicepicker.model.device.specifications.AdditionalInfo.Companion.toMap
import com.ravenzip.devicepicker.model.device.specifications.Audio
import com.ravenzip.devicepicker.model.device.specifications.Audio.Companion.toMap
import com.ravenzip.devicepicker.model.device.specifications.BaseInfo
import com.ravenzip.devicepicker.model.device.specifications.BaseInfo.Companion.toMap
import com.ravenzip.devicepicker.model.device.specifications.Camera
import com.ravenzip.devicepicker.model.device.specifications.Communication
import com.ravenzip.devicepicker.model.device.specifications.Communication.Companion.toMap
import com.ravenzip.devicepicker.model.device.specifications.Connectors
import com.ravenzip.devicepicker.model.device.specifications.Connectors.Companion.toMap
import com.ravenzip.devicepicker.model.device.specifications.Construction
import com.ravenzip.devicepicker.model.device.specifications.Construction.Companion.toMap
import com.ravenzip.devicepicker.model.device.specifications.Cpu
import com.ravenzip.devicepicker.model.device.specifications.Cpu.Companion.toMap
import com.ravenzip.devicepicker.model.device.specifications.Dimensions
import com.ravenzip.devicepicker.model.device.specifications.Dimensions.Companion.toMap
import com.ravenzip.devicepicker.model.device.specifications.Memory
import com.ravenzip.devicepicker.model.device.specifications.Memory.Companion.toMap
import com.ravenzip.devicepicker.model.device.specifications.OperationSystem
import com.ravenzip.devicepicker.model.device.specifications.OperationSystem.Companion.toMap
import com.ravenzip.devicepicker.model.device.specifications.Power
import com.ravenzip.devicepicker.model.device.specifications.Power.Companion.toMap
import com.ravenzip.devicepicker.model.device.specifications.Screen
import com.ravenzip.devicepicker.model.device.specifications.Screen.Companion.toMap
import com.ravenzip.devicepicker.model.device.specifications.camera.BackCamera.Companion.toMap
import com.ravenzip.devicepicker.model.device.specifications.camera.FrontCamera.Companion.toMap

class DeviceSpecifications(
    val baseInfo: BaseInfo,
    val screen: Screen,
    val construction: Construction,
    val operationSystem: OperationSystem,
    val cpu: Cpu,
    val memory: Memory,
    val camera: Camera,
    val audio: Audio,
    val communication: Communication,
    val connectors: Connectors,
    val power: Power,
    val additionalInfo: AdditionalInfo,
    val dimensions: Dimensions
) {
    constructor() :
        this(
            baseInfo = BaseInfo(),
            screen = Screen(),
            construction = Construction(),
            operationSystem = OperationSystem(),
            cpu = Cpu(),
            memory = Memory(),
            camera = Camera(),
            audio = Audio(),
            communication = Communication(),
            connectors = Connectors(),
            power = Power(),
            additionalInfo = AdditionalInfo(),
            dimensions = Dimensions())

    companion object {
        fun DeviceSpecifications.toMap(): Map<String, Map<String, String>> {
            return mapOf(
                "Общая информация" to this.baseInfo.toMap(),
                "Экран" to this.screen.toMap(),
                "Корпус и защита" to this.construction.toMap(),
                "Операционная система" to this.operationSystem.toMap(),
                "Процессор" to this.cpu.toMap(),
                "Память" to this.memory.toMap(),
                "Основная камера" to this.camera.back.toMap(),
                "Фронтальная камера" to this.camera.front.toMap(),
                "Аудио" to this.audio.toMap(),
                "Сеть" to this.communication.toMap(),
                "Разъемы" to this.connectors.toMap(),
                "Питание" to this.power.toMap(),
                "Дополнительная информация" to this.additionalInfo.toMap(),
                "Габариты, вес" to this.dimensions.toMap())
        }
    }
}
