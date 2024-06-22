package com.ravenzip.devicepicker.state

import com.ravenzip.devicepicker.model.device.compact.DeviceCompact

/**
 * Состояние данных об устройстве (полная модель)
 *
 * [deviceCompactList] - Список устройств (компактная модель)
 *
 * [popularDevices] - Список устройств с меткой "Популярные"
 *
 * [lowPriceDevices] - Список устройств с меткой "Низкая цена"
 *
 * [highPerformanceDevices] - Список устройств с меткой "Производительные"
 *
 * [theBestDevices] - Список устройств с меткой "Лучшие устройства бренда"
 *
 * [recentlyViewedDevices] - Список недавно просмотренных устройств
 */
data class DeviceCompactState(
    val deviceCompactList: MutableList<DeviceCompact>,
    val popularDevices: MutableList<DeviceCompact>,
    val lowPriceDevices: MutableList<DeviceCompact>,
    val highPerformanceDevices: MutableList<DeviceCompact>,
    val theBestDevices: MutableList<MutableList<DeviceCompact>>,
    val recentlyViewedDevices: MutableList<DeviceCompact>,
) {
    constructor() :
        this(
            deviceCompactList = mutableListOf(),
            popularDevices = mutableListOf(),
            lowPriceDevices = mutableListOf(),
            highPerformanceDevices = mutableListOf(),
            theBestDevices = mutableListOf(),
            recentlyViewedDevices = mutableListOf(),
        )
}
