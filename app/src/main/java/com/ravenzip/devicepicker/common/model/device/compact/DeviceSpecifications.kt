package com.ravenzip.devicepicker.common.model.device.compact

import com.ravenzip.devicepicker.common.model.device.specifications.AdditionalInfoDto
import com.ravenzip.devicepicker.common.model.device.specifications.AdditionalInfoDto.Companion.toMap
import com.ravenzip.devicepicker.common.model.device.specifications.AudioDto
import com.ravenzip.devicepicker.common.model.device.specifications.AudioDto.Companion.toMap
import com.ravenzip.devicepicker.common.model.device.specifications.BaseInfoDto
import com.ravenzip.devicepicker.common.model.device.specifications.BaseInfoDto.Companion.toMap
import com.ravenzip.devicepicker.common.model.device.specifications.CameraDto
import com.ravenzip.devicepicker.common.model.device.specifications.CommunicationDto
import com.ravenzip.devicepicker.common.model.device.specifications.CommunicationDto.Companion.toMap
import com.ravenzip.devicepicker.common.model.device.specifications.ConnectorsDto
import com.ravenzip.devicepicker.common.model.device.specifications.ConnectorsDto.Companion.toMap
import com.ravenzip.devicepicker.common.model.device.specifications.ConstructionDto
import com.ravenzip.devicepicker.common.model.device.specifications.ConstructionDto.Companion.toMap
import com.ravenzip.devicepicker.common.model.device.specifications.CpuDto
import com.ravenzip.devicepicker.common.model.device.specifications.CpuDto.Companion.toMap
import com.ravenzip.devicepicker.common.model.device.specifications.DimensionsDto
import com.ravenzip.devicepicker.common.model.device.specifications.DimensionsDto.Companion.toMap
import com.ravenzip.devicepicker.common.model.device.specifications.MemoryDto
import com.ravenzip.devicepicker.common.model.device.specifications.MemoryDto.Companion.toMap
import com.ravenzip.devicepicker.common.model.device.specifications.OperationSystemDto
import com.ravenzip.devicepicker.common.model.device.specifications.OperationSystemDto.Companion.toMap
import com.ravenzip.devicepicker.common.model.device.specifications.PowerDto
import com.ravenzip.devicepicker.common.model.device.specifications.PowerDto.Companion.toMap
import com.ravenzip.devicepicker.common.model.device.specifications.ScreenDto
import com.ravenzip.devicepicker.common.model.device.specifications.ScreenDto.Companion.toMap
import com.ravenzip.devicepicker.common.model.device.specifications.camera.BackCameraDto.Companion.toMap
import com.ravenzip.devicepicker.common.model.device.specifications.camera.FrontCameraDto.Companion.toMap

class DeviceSpecifications(
    val baseInfo: BaseInfoDto,
    val screen: ScreenDto,
    val construction: ConstructionDto,
    val operationSystem: OperationSystemDto,
    val cpu: CpuDto,
    val memory: MemoryDto,
    val camera: CameraDto,
    val audio: AudioDto,
    val communication: CommunicationDto,
    val connectors: ConnectorsDto,
    val power: PowerDto,
    val additionalInfo: AdditionalInfoDto,
    val dimensions: DimensionsDto,
) {
    constructor() :
        this(
            baseInfo = BaseInfoDto(),
            screen = ScreenDto(),
            construction = ConstructionDto(),
            operationSystem = OperationSystemDto(),
            cpu = CpuDto(),
            memory = MemoryDto(),
            camera = CameraDto(),
            audio = AudioDto(),
            communication = CommunicationDto(),
            connectors = ConnectorsDto(),
            power = PowerDto(),
            additionalInfo = AdditionalInfoDto(),
            dimensions = DimensionsDto(),
        )

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
                "Габариты, вес" to this.dimensions.toMap(),
            )
        }
    }
}
