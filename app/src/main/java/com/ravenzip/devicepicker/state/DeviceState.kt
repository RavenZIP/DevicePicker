package com.ravenzip.devicepicker.state

import com.ravenzip.devicepicker.model.device.Device

/**
 * Состояние данных об устройстве (полная модель)
 *
 * [device] - Текущее выбранное устройство
 *
 * [deviceList] - Кэш устройств на время работы с приложением
 */
class DeviceState(var device: Device, val deviceList: MutableList<Device>) {
    constructor() : this(device = Device(), deviceList = mutableListOf())
}
