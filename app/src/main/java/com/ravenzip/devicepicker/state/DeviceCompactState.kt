package com.ravenzip.devicepicker.state

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
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
    val userSearchHistoryUidList: SnapshotStateList<String>,
    val deviceCategoryStateList: SnapshotStateList<DeviceCategoryState>
) {
    constructor() :
        this(
            deviceCompactList = mutableStateListOf(),
            userSearchHistoryUidList = mutableStateListOf(),
            deviceCategoryStateList = mutableStateListOf())

    fun DeviceCompactState.popularDevicesState(): DeviceCategoryState {
        val devices =
            this.deviceCompactList
                .filter { it.tags.contains(TagsEnum.POPULAR) }
                .toMutableStateList()

        return DeviceCategoryState(
            devices = devices,
            containerType = ContainerTypeEnum.Default,
            categoryName = categoryNameMap[TagsEnum.POPULAR]!!)
    }

    fun DeviceCompactState.lowPriceDevices(): DeviceCategoryState {
        val devices =
            this.deviceCompactList
                .filter { it.tags.contains(TagsEnum.LOW_PRICE) }
                .toMutableStateList()

        return DeviceCategoryState(
            devices = devices,
            containerType = ContainerTypeEnum.Default,
            categoryName = categoryNameMap[TagsEnum.LOW_PRICE]!!)
    }

    fun DeviceCompactState.highPerformanceDevices(): DeviceCategoryState {
        val devices =
            this.deviceCompactList
                .filter { it.tags.contains(TagsEnum.HIGH_PERFORMANCE) }
                .toMutableStateList()

        return DeviceCategoryState(
            devices = devices,
            containerType = ContainerTypeEnum.Default,
            categoryName = categoryNameMap[TagsEnum.HIGH_PERFORMANCE]!!)
    }

    fun DeviceCompactState.energyEfficientDevices(): DeviceCategoryState {
        val devices =
            this.deviceCompactList
                .filter { it.tags.contains(TagsEnum.ENERGY_EFFICIENT) }
                .toMutableStateList()

        return DeviceCategoryState(
            devices = devices,
            containerType = ContainerTypeEnum.Default,
            categoryName = categoryNameMap[TagsEnum.ENERGY_EFFICIENT]!!)
    }

    fun DeviceCompactState.reliableDevices(): DeviceCategoryState {
        val devices =
            this.deviceCompactList
                .filter { it.tags.contains(TagsEnum.RELIABLE) }
                .toMutableStateList()

        return DeviceCategoryState(
            devices = devices,
            containerType = ContainerTypeEnum.Default,
            categoryName = categoryNameMap[TagsEnum.RELIABLE]!!)
    }

    fun DeviceCompactState.newModelDevices(): DeviceCategoryState {
        val devices =
            this.deviceCompactList
                .filter { it.tags.contains(TagsEnum.NEW_MODEL) }
                .toMutableStateList()

        return DeviceCategoryState(
            devices = devices,
            containerType = ContainerTypeEnum.Special,
            categoryName = categoryNameMap[TagsEnum.NEW_MODEL]!!)
    }

    fun DeviceCompactState.highQualityScreenDevices(): DeviceCategoryState {
        val devices =
            this.deviceCompactList
                .filter { it.tags.contains(TagsEnum.HIGH_QUALITY_SCREEN) }
                .toMutableStateList()

        return DeviceCategoryState(
            devices = devices,
            containerType = ContainerTypeEnum.Special,
            categoryName = categoryNameMap[TagsEnum.HIGH_QUALITY_SCREEN]!!)
    }

    fun DeviceCompactState.userSearchHistoryDevices(): DeviceCategoryState {
        val devices =
            this.deviceCompactList
                .filter { userSearchHistoryUidList.contains(it.uid) }
                .toMutableStateList()

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
                this.userSearchHistoryDevices())
        }
    }
}
