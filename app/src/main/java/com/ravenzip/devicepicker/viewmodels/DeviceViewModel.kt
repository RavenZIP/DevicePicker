package com.ravenzip.devicepicker.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.constants.enums.TagsEnum
import com.ravenzip.devicepicker.model.device.compact.DeviceCompact
import com.ravenzip.devicepicker.model.result.ImageUrlResult
import com.ravenzip.devicepicker.model.result.Result
import com.ravenzip.devicepicker.repositories.DeviceRepository
import com.ravenzip.devicepicker.repositories.ImageRepository
import com.ravenzip.devicepicker.state.DeviceCompactState
import com.ravenzip.devicepicker.state.DeviceState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.onEach
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
    /** Состояние данных об устройстве (компактная модель) */
    private val _deviceCompactState = MutableStateFlow(DeviceCompactState())

    /** Состояние данных об устройстве (полная модель) */
    private val _deviceState = MutableStateFlow(DeviceState())

    val deviceCompactState = _deviceCompactState.asStateFlow()
    val deviceState = _deviceState.asStateFlow()

    /** Получение списка устройств и их изображений (компактная модель) */
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getDeviceCompactList() {
        deviceRepository
            .getDeviceCompactList()
            .onEach { devices ->
                _deviceCompactState.value.allDevices.addAll(devices)
                createCategories()
            }
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
            _deviceCompactState.value.allDevices.indexOfFirst { device ->
                device.uid == imageUrl.deviceUid
            }

        if (deviceIndex != -1) {
            _deviceCompactState.value.allDevices[deviceIndex] =
                _deviceCompactState.value.allDevices[deviceIndex].copy(imageUrl = imageUrl.value)
        }

        _deviceCompactState.value.categories.forEach { (key, value) ->
            value.forEachIndexed { index, device ->
                if (device.uid == imageUrl.deviceUid) {
                    _deviceCompactState.value.categories[key]!![index] =
                        _deviceCompactState.value.categories[key]!![index].copy(
                            imageUrl = imageUrl.value
                        )
                }
            }
        }
    }

    /** Создание категорий устройств в соответствии с тегами */
    private fun createCategories() {
        // Расставляем ключи в том порядке, в котором они заданы в энуме
        val categories = linkedMapOf<TagsEnum, SnapshotStateList<DeviceCompact>>()
        TagsEnum.entries.forEach { tag -> categories[tag] = mutableStateListOf() }

        _deviceCompactState.value.allDevices.forEach { device ->
            device.tags.forEach { tag -> categories[tag]!!.add(device) }
        }

        _deviceCompactState.value.categories.putAll(categories)
    }
}
