package com.ravenzip.devicepicker.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.ravenzip.devicepicker.constants.enums.TagsEnum
import com.ravenzip.devicepicker.constants.map.tagIconMap
import com.ravenzip.devicepicker.model.device.compact.DeviceCompact
import com.ravenzip.workshop.data.TextConfig
import com.ravenzip.workshop.data.icon.IconConfig
import com.ravenzip.workshop.data.selection.SelectableChipConfig

/**
 * Состояние данных об устройстве (полная модель)
 *
 * [allDevices] - Все устройства (компактная модель)
 *
 * [userSearchHistoryUidList] - Список uid устройств, которые просматривал пользователь
 *
 * [categories] - Все устройства, отсортированные по категориям
 */
class DeviceCompactState(
    val allDevices: SnapshotStateList<DeviceCompact>,
    val userSearchHistoryUidList: SnapshotStateList<String>,
    val categories: LinkedHashMap<TagsEnum, SnapshotStateList<DeviceCompact>>,
) {
    constructor() :
        this(
            allDevices = mutableStateListOf(),
            userSearchHistoryUidList = mutableStateListOf(),
            categories = linkedMapOf(),
        )

    companion object {
        @Composable
        fun DeviceCompactState.listOfCategories(): SnapshotStateList<SelectableChipConfig> {
            val listOfCategories =
                this.categories.keys
                    .mapIndexed { index, category ->
                        SelectableChipConfig(
                            isSelected = index == 0,
                            text = category.value,
                            textConfig = TextConfig.SmallMedium,
                            icon = ImageVector.vectorResource(id = tagIconMap[category]!!),
                            iconConfig = IconConfig.Small,
                        )
                    }
                    .toMutableStateList()

            return remember(categories.keys.count()) { listOfCategories }
        }
    }
}
