package com.ravenzip.devicepicker.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.SharedRepository
import com.ravenzip.devicepicker.constants.enums.TagsEnum
import com.ravenzip.devicepicker.constants.map.tagIconMap
import com.ravenzip.devicepicker.constants.map.tagsColorMap
import com.ravenzip.devicepicker.model.device.compact.DeviceCompact
import com.ravenzip.workshop.data.TextConfig
import com.ravenzip.workshop.data.icon.Icon
import com.ravenzip.workshop.data.icon.IconConfig
import com.ravenzip.workshop.data.selection.SelectableChipConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val sharedRepository: SharedRepository) :
    ViewModel() {
    /** Все устройства (компактная модель) */
    private val _allDevices = sharedRepository.allDevices

    /** Категории устройств */
    private val _categories = MutableStateFlow(mutableStateListOf<SelectableChipConfig>())

    /** Все устройства, сгруппированные по категориям */
    private val _categorizedDevices =
        _allDevices.map { devices ->
            // Расставляем ключи в том порядке, в котором они заданы в энуме
            val categorizedDevices = linkedMapOf<TagsEnum, SnapshotStateList<DeviceCompact>>()
            TagsEnum.entries.forEach { tag -> categorizedDevices[tag] = mutableStateListOf() }

            devices.forEach { device ->
                device.tags.forEach { tag -> categorizedDevices[tag]!!.add(device) }
            }

            return@map categorizedDevices
        }

    val categories = _categories.asStateFlow()

    val selectedCategory =
        _categories
            .combine(_categorizedDevices) { categories, categorizedDevices ->
                val selectedCategoryName =
                    categories.firstOrNull { category -> category.isSelected }?.text
                val selectedCategory =
                    TagsEnum.entries.firstOrNull { tag -> tag.value == selectedCategoryName }

                val devices = categorizedDevices[selectedCategory] ?: mutableStateListOf()

                return@combine devices
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = mutableStateListOf(),
            )

    init {
        Log.d("HomeScreenViewModel", "init")
        viewModelScope.launch { sharedRepository.getDeviceCompactList() }

        viewModelScope.launch {
            _categorizedDevices.collect { categorizedDevices ->
                val categories =
                    categorizedDevices.keys
                        .mapIndexed { index, category ->
                            SelectableChipConfig(
                                isSelected = index == 0,
                                text = category.value,
                                textConfig = TextConfig.SmallMedium,
                                icon = Icon.ResourceIcon(id = tagIconMap[category]!!),
                                iconConfig = IconConfig(size = 20, color = tagsColorMap[category]),
                            )
                        }
                        .toMutableStateList()

                _categories.update { categories }
            }
        }
    }

    fun selectCategory(item: SelectableChipConfig) {
        val updatedCategories = _categories.value.toMutableList()
        updatedCategories.replaceAll { it.copy(isSelected = it.text == item.text) }

        _categories.update { updatedCategories.toMutableStateList() }
    }
}
