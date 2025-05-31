package com.ravenzip.devicepicker.common.enums

import com.ravenzip.devicepicker.R

enum class DeviceTypeEnum(val value: String, val icon: Int) {
    SMARTPHONE(value = "Смартфон", icon = R.drawable.i_smartphone),
    TABLET(value = "Планшет", icon = R.drawable.i_tablet),
    LAPTOP(value = "Ноутбук", icon = R.drawable.i_laptop),
    SMART_WATCH(value = "Смарт-часы", icon = R.drawable.i_smart_watch),
    FITNESS_WATCH(value = "Фитнес-браслет", icon = R.drawable.i_fitness_watch),
}
