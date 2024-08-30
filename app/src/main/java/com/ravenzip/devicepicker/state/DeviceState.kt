package com.ravenzip.devicepicker.state

import com.ravenzip.devicepicker.model.device.Device
import com.ravenzip.devicepicker.model.result.Result

/**
 * Состояние данных об устройстве (полная модель)
 *
 * [device] - Текущее выбранное устройство
 *
 * [deviceList] - Кэш устройств на время работы с приложением
 */
data class DeviceState(val device: Result<Device?>, val deviceList: MutableList<Device>) {
    constructor() : this(device = Result.default(), deviceList = mutableListOf())
}
