package com.ravenzip.devicepicker.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.constants.enums.TagsEnum
import com.ravenzip.devicepicker.constants.map.tagIconMap
import com.ravenzip.devicepicker.constants.map.tagsColorMap
import com.ravenzip.devicepicker.model.device.compact.DeviceCompact
import com.ravenzip.devicepicker.model.result.ImageUrlResult
import com.ravenzip.devicepicker.model.result.Result
import com.ravenzip.devicepicker.repositories.DeviceRepository
import com.ravenzip.devicepicker.repositories.ImageRepository
import com.ravenzip.devicepicker.state.DeviceState
import com.ravenzip.workshop.data.TextConfig
import com.ravenzip.workshop.data.icon.Icon
import com.ravenzip.workshop.data.icon.IconConfig
import com.ravenzip.workshop.data.selection.SelectableChipConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

@HiltViewModel
class DeviceViewModel
@Inject
constructor(
    private val deviceRepository: DeviceRepository,
    private val imageRepository: ImageRepository,
) : ViewModel() {
    /** Все устройства (компактная модель) */
    private val _allDevices = MutableStateFlow(mutableStateListOf<DeviceCompact>())

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

    /** Состояние данных об устройстве (полная модель) */
    private val _deviceState = MutableStateFlow(DeviceState())

    val deviceState = _deviceState.asStateFlow()
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

    fun selectCategory(item: SelectableChipConfig) {
        val updatedCategories = _categories.value.toMutableList()
        updatedCategories.replaceAll { it.copy(isSelected = it.text == item.text) }

        _categories.update { updatedCategories.toMutableStateList() }
    }

    init {
        viewModelScope.launch { getDeviceCompactList() }

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

    /** Получение списка устройств и их изображений (компактная модель) */
    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun getDeviceCompactList() {
        deviceRepository
            .getDeviceCompactList()
            .onEach { devices -> _allDevices.update { devices.toMutableStateList() } }
            .flatMapLatest { devices -> imageRepository.getImageUrls(devices) }
            .flatMapMerge(concurrency = 3) { it }
            .collect { imageUrl -> setImageUrlToDevices(imageUrl) }
    }

    /** Получение устройства по бренду и уникальному идентификатору */
    private suspend fun getDeviceByBrandAndUid(uid: String, brand: String, model: String) {
        deviceRepository
            .getDeviceByBrandAndUid(brand, uid)
            .zip(imageRepository.getImageUrls(brand, model)) { device, imageUrls ->
                if (device != null) {
                    val deviceWithImageUrls = device.copy(imageUrls = imageUrls)
                    val updatedDeviceList = _deviceState.value.deviceList + deviceWithImageUrls

                    _deviceState.update {
                        DeviceState(
                            device = Result.success(deviceWithImageUrls),
                            deviceList = updatedDeviceList.toMutableList(),
                        )
                    }
                } else {
                    val errorMessage = "При выполении запроса произошла ошибка"
                    _deviceState.update {
                        _deviceState.value.copy(device = Result.error(errorMessage = errorMessage))
                    }
                }
            }
            .collect {}
    }

    /**
     * Получение полной информации об устройстве
     *
     * Сначала идет попытка получения закешированного устройства. Если его нет, то выполняется
     * запрос
     *
     * @param uid уникальный идентификатор устройства
     * @param brand бренд устройства
     * @param model модель устройства
     */
    fun getDevice(uid: String, brand: String, model: String) {
        viewModelScope.launch {
            _deviceState.update { _deviceState.value.copy(device = Result.loading()) }
            val cachedDevice = _deviceState.value.deviceList.find { device -> device.uid == uid }

            if (cachedDevice == null) {
                getDeviceByBrandAndUid(uid, brand, model)
            } else {
                _deviceState.update {
                    _deviceState.value.copy(device = Result.success(cachedDevice))
                }
            }
        }
    }

    /** Заполнить урл изображения для конкретного устройства */
    private fun setImageUrlToDevices(imageUrl: ImageUrlResult<String>) {
        val deviceIndex =
            _allDevices.value.indexOfFirst { device -> device.uid == imageUrl.deviceUid }

        if (deviceIndex != -1) {
            val updatedAllDevices = _allDevices.value.toMutableList()
            updatedAllDevices[deviceIndex] =
                updatedAllDevices[deviceIndex].copy(imageUrl = imageUrl.value)

            _allDevices.update { updatedAllDevices.toMutableStateList() }
        }
    }

    fun createDeviceHistoryList(userDeviceHistoryUidList: List<String>): List<DeviceCompact> {
        return _allDevices.value.filter { device -> device.uid in userDeviceHistoryUidList }
    }
}
