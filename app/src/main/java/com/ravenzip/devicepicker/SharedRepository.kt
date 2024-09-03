package com.ravenzip.devicepicker

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import com.ravenzip.devicepicker.model.device.Device
import com.ravenzip.devicepicker.model.device.compact.DeviceCompact
import com.ravenzip.devicepicker.model.result.ImageUrlResult
import com.ravenzip.devicepicker.model.result.Result
import com.ravenzip.devicepicker.repositories.DeviceRepository
import com.ravenzip.devicepicker.repositories.ImageRepository
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip

class SharedRepository
@Inject
constructor(
    private val deviceRepository: DeviceRepository,
    private val imageRepository: ImageRepository,
) {
    /** Все устройства (компактная модель) */
    private val _allDevices = MutableStateFlow(mutableStateListOf<DeviceCompact>())

    /** Кэш устройств на время работы с приложением */
    private val _cachedDevices = MutableStateFlow(listOf<Device>())

    /** История просмотренных устройств */
    private val _deviceHistoryUid = MutableStateFlow(listOf<String>())

    /** Текущее выбранное устройство */
    private val _device = MutableStateFlow(Result.default<Device>())

    val allDevices = _allDevices.asStateFlow()
    val deviceHistoryUid = _deviceHistoryUid.asStateFlow()
    val device = _device.asStateFlow()

    /** Получение списка устройств и их изображений (компактная модель) */
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getDeviceCompactList() {
        deviceRepository
            .getDeviceCompactList()
            .onEach { devices -> _allDevices.update { devices.toMutableStateList() } }
            .flatMapLatest { devices -> imageRepository.getImageUrls(devices) }
            .flatMapMerge(concurrency = 3) { it }
            .collect { imageUrl -> setImageUrlToDevices(imageUrl) }
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

    /** Получение устройства по бренду и уникальному идентификатору */
    private suspend fun getDeviceByBrandAndUid(uid: String, brand: String, model: String) {
        deviceRepository
            .getDeviceByBrandAndUid(brand, uid)
            .zip(imageRepository.getImageUrls(brand, model)) { device, imageUrls ->
                if (device != null) {
                    val deviceWithImageUrls = device.copy(imageUrls = imageUrls)
                    val updatedDeviceList = _cachedDevices.value + deviceWithImageUrls

                    _device.update { Result.success(deviceWithImageUrls) }
                    _cachedDevices.update { updatedDeviceList }
                } else {
                    val errorMessage = "При выполении запроса произошла ошибка"
                    _device.update { Result.error(errorMessage = errorMessage) }
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
    suspend fun getDevice(uid: String, brand: String, model: String) {
        _device.update { Result.loading() }
        val cachedDevice = _cachedDevices.value.find { device -> device.uid == uid }

        if (cachedDevice == null) {
            getDeviceByBrandAndUid(uid, brand, model)
        } else {
            _device.update { Result.success(cachedDevice) }
        }
    }
}
