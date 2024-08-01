package com.ravenzip.devicepicker.state

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.ravenzip.devicepicker.constants.enums.ContainerTypeEnum
import com.ravenzip.devicepicker.constants.enums.TagsEnum
import com.ravenzip.devicepicker.constants.map.categoryNameMap
import com.ravenzip.devicepicker.model.device.compact.DeviceCompact

/**
 * Состояние данных об устройстве (полная модель)
 *
 * [deviceCompactList] - Список устройств (компактная модель)
 *
 * [userSearchHistoryUidList] - Список uid устройств, которые просматривал пользователь
 */
class DeviceCompactState(
    val deviceCompactList: SnapshotStateList<DeviceCompact>,
    val userSearchHistoryUidList: SnapshotStateList<String>
) {
    constructor() :
        this(
            deviceCompactList = mutableStateListOf(),
            userSearchHistoryUidList = mutableStateListOf())

    fun DeviceCompactState.popularDevicesState(): DeviceCategoryState {
        val devices = this.deviceCompactList.filter { it.tags.contains(TagsEnum.POPULAR) }
        return DeviceCategoryState(
            devices = devices,
            containerType = ContainerTypeEnum.Default,
            categoryName = categoryNameMap[TagsEnum.POPULAR]!!)
    }

    fun DeviceCompactState.lowPriceDevices(): DeviceCategoryState {
        val devices = this.deviceCompactList.filter { it.tags.contains(TagsEnum.LOW_PRICE) }
        return DeviceCategoryState(
            devices = devices,
            containerType = ContainerTypeEnum.Default,
            categoryName = categoryNameMap[TagsEnum.LOW_PRICE]!!)
    }

    fun DeviceCompactState.highPerformanceDevices(): DeviceCategoryState {
        val devices = this.deviceCompactList.filter { it.tags.contains(TagsEnum.HIGH_PERFORMANCE) }
        return DeviceCategoryState(
            devices = devices,
            containerType = ContainerTypeEnum.Default,
            categoryName = categoryNameMap[TagsEnum.HIGH_PERFORMANCE]!!)
    }

    fun DeviceCompactState.energyEfficientDevices(): DeviceCategoryState {
        val devices = this.deviceCompactList.filter { it.tags.contains(TagsEnum.ENERGY_EFFICIENT) }
        return DeviceCategoryState(
            devices = devices,
            containerType = ContainerTypeEnum.Default,
            categoryName = categoryNameMap[TagsEnum.ENERGY_EFFICIENT]!!)
    }

    fun DeviceCompactState.reliableDevices(): DeviceCategoryState {
        val devices = this.deviceCompactList.filter { it.tags.contains(TagsEnum.RELIABLE) }
        return DeviceCategoryState(
            devices = devices,
            containerType = ContainerTypeEnum.Default,
            categoryName = categoryNameMap[TagsEnum.RELIABLE]!!)
    }

    fun DeviceCompactState.newModelDevices(): DeviceCategoryState {
        val devices = this.deviceCompactList.filter { it.tags.contains(TagsEnum.NEW_MODEL) }
        return DeviceCategoryState(
            devices = devices,
            containerType = ContainerTypeEnum.Special,
            categoryName = categoryNameMap[TagsEnum.NEW_MODEL]!!)
    }

    fun DeviceCompactState.highQualityScreenDevices(): DeviceCategoryState {
        val devices =
            this.deviceCompactList.filter { it.tags.contains(TagsEnum.HIGH_QUALITY_SCREEN) }
        return DeviceCategoryState(
            devices = devices,
            containerType = ContainerTypeEnum.Special,
            categoryName = categoryNameMap[TagsEnum.HIGH_QUALITY_SCREEN]!!)
    }

    fun DeviceCompactState.highQualityConnectionDevices(): DeviceCategoryState {
        val devices =
            this.deviceCompactList.filter { it.tags.contains(TagsEnum.HIGH_QUALITY_CONNECTION) }
        return DeviceCategoryState(
            devices = devices,
            containerType = ContainerTypeEnum.Special,
            categoryName = categoryNameMap[TagsEnum.HIGH_QUALITY_CONNECTION]!!)
    }

    fun DeviceCompactState.highQualityCameraDevices(): DeviceCategoryState {
        val devices =
            this.deviceCompactList.filter { it.tags.contains(TagsEnum.HIGH_QUALITY_CAMERA) }
        return DeviceCategoryState(
            devices = devices,
            containerType = ContainerTypeEnum.Special,
            categoryName = categoryNameMap[TagsEnum.HIGH_QUALITY_CAMERA]!!)
    }

    fun DeviceCompactState.userSearchHistoryDevices(): DeviceCategoryState {
        val devices = this.deviceCompactList.filter { userSearchHistoryUidList.contains(it.uid) }
        return DeviceCategoryState(
            devices = devices,
            containerType = ContainerTypeEnum.Default,
            categoryName = "Вы недавно смотрели")
    }

    companion object {
        fun DeviceCompactState.deviceCompactStateToList(): List<DeviceCategoryState> {
            return listOf(
                this.popularDevicesState(),
                this.lowPriceDevices(),
                this.newModelDevices(),
                this.highPerformanceDevices(),
                this.energyEfficientDevices(),
                this.highQualityScreenDevices(),
                this.reliableDevices(),
                this.highQualityConnectionDevices(),
                this.highQualityCameraDevices(),
                this.userSearchHistoryDevices())
        }
    }
}
