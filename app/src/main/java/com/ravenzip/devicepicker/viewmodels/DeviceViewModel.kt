package com.ravenzip.devicepicker.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.ravenzip.devicepicker.constants.enums.TagsEnum
import com.ravenzip.devicepicker.model.device.Device
import com.ravenzip.devicepicker.model.device.compact.DeviceCompact
import com.ravenzip.devicepicker.model.result.ImageUrlResult
import com.ravenzip.devicepicker.repositories.DeviceRepository
import com.ravenzip.devicepicker.repositories.ImageRepository
import com.ravenzip.devicepicker.state.DeviceCompactState
import com.ravenzip.devicepicker.state.DeviceState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.zip

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
            .flatMapLatest { imageRepository.getImageUrls(_deviceCompactState.value.allDevices) }
            .flatMapMerge(concurrency = 3) { it }
            .collect { imageUrl -> setImageUrlToDevices(imageUrl) }
    }

    /** Получить закешированное устройство */
    fun getCachedDevice(uid: String): Device? {
        val cachedDevice = _deviceState.value.deviceList.find { device -> device.uid == uid }
        if (cachedDevice !== null) {
            _deviceState.value.device = cachedDevice
        }

        return cachedDevice
    }

    /** Получение устройства по бренду и уникальному идентификатору */
    suspend fun getDeviceByBrandAndUid(uid: String, brand: String, model: String) {
        deviceRepository
            .getDeviceByBrandAndUid(brand, uid)
            .zip(imageRepository.getImageUrls(brand, model)) { device, imageUrls ->
                if (device != null) {
                    val deviceWithImageUrls = device.copy(imageUrls = imageUrls)
                    deviceState.value.device = deviceWithImageUrls
                    deviceState.value.deviceList.add(deviceWithImageUrls)
                }
            }
            .collect {}
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
