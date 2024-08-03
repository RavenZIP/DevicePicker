package com.ravenzip.devicepicker.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.ravenzip.devicepicker.model.device.Device
import com.ravenzip.devicepicker.model.device.compact.DeviceCompact
import com.ravenzip.devicepicker.model.result.ImageUrlResult
import com.ravenzip.devicepicker.repositories.DeviceRepository
import com.ravenzip.devicepicker.state.DeviceCompactState
import com.ravenzip.devicepicker.state.DeviceCompactState.Companion.deviceCompactStateToList
import com.ravenzip.devicepicker.state.DeviceState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

@HiltViewModel
class DeviceViewModel @Inject constructor(private val deviceRepository: DeviceRepository) :
    ViewModel() {
    /** Состояние данных об устройстве (компактная модель) */
    private val _deviceCompactState = MutableStateFlow(DeviceCompactState())

    /** Состояние данных об устройстве (полная модель) */
    private val _deviceState = MutableStateFlow(DeviceState())

    val deviceCompactState = _deviceCompactState.asStateFlow()
    val deviceState = _deviceState.asStateFlow()

    /** Получение списка устройств (компактная модель) */
    suspend fun getDeviceCompactList(): Flow<List<DeviceCompact>> =
        flow {
                val devices =
                    deviceRepository.getDeviceCompactList().map { device ->
                        device.convertToDeviceCompact()
                    }
                _deviceCompactState.value.deviceCompactList.addAll(devices)

                emit(devices)
            }
            .catch {
                withContext(Dispatchers.Main) { Log.e("DeviceCompactViewModel", "${it.message}") }

                emit(listOf())
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
    suspend fun getDeviceByBrandAndUid(brand: String, uid: String): Flow<Boolean> =
        flow {
                val firebaseDevice = deviceRepository.getDeviceByBrandAndUid(brand, uid)
                val device = firebaseDevice.convertToDevice()
                deviceState.value.device = device
                deviceState.value.deviceList.add(device)

                emit(true)
            }
            .catch {
                withContext(Dispatchers.Main) { Log.e("DeviceCompactViewModel", "${it.message}") }
                emit(false)
            }

    fun setUserSearchHistoryUidList(userSearchHistory: List<String>) {
        deviceCompactState.value.userSearchHistoryUidList.addAll(userSearchHistory)
    }

    /** Заполнить урл изображения для конкретного устройства */
    fun setImageUrlToDevices(imageUrl: ImageUrlResult<String>) {
        val deviceIndex =
            _deviceCompactState.value.deviceCompactList.indexOfFirst { device ->
                device.uid == imageUrl.deviceUid
            }

        _deviceCompactState.value.deviceCategoryStateList.forEachIndexed {
            indexOfDeviceCategory,
            deviceCategoryState ->
            deviceCategoryState.devices.forEachIndexed { indexOfDevice, deviceCompact ->
                if (deviceCompact.uid == imageUrl.deviceUid) {
                    _deviceCompactState.value.deviceCategoryStateList[indexOfDeviceCategory]
                        .devices[indexOfDevice] =
                        _deviceCompactState.value.deviceCategoryStateList[indexOfDeviceCategory]
                            .devices[indexOfDevice]
                            .copy(imageUrl = imageUrl.value)
                }
            }
        }

        if (deviceIndex != -1) {
            _deviceCompactState.value.deviceCompactList[deviceIndex] =
                _deviceCompactState.value.deviceCompactList[deviceIndex].copy(
                    imageUrl = imageUrl.value)
        }
    }

    /** Заполнить урл изображения для конкретного устройства */
    fun setImageUrlToDevices(imageUrls: List<String>) {
        _deviceState.value.device = _deviceState.value.device.copy(imageUrls = imageUrls)

        val deviceIndex =
            _deviceState.value.deviceList.indexOfFirst { device ->
                device.uid == _deviceState.value.device.uid
            }

        if (deviceIndex != -1) {
            _deviceState.value.deviceList[deviceIndex] =
                _deviceState.value.deviceList[deviceIndex].copy(imageUrls = imageUrls)
        }
    }

    fun createDeviceCompactStateList() {
        _deviceCompactState.value.deviceCategoryStateList.addAll(
            _deviceCompactState.value.deviceCompactStateToList())
    }
}
