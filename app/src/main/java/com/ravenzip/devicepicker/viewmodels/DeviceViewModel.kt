package com.ravenzip.devicepicker.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.ravenzip.devicepicker.data.device.Device
import com.ravenzip.devicepicker.data.device.FirebaseImageData
import com.ravenzip.devicepicker.data.device.compact.DeviceCompact
import com.ravenzip.devicepicker.repositories.DeviceRepository
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
    /** Список устройств (полная модель) */
    // Является неким кэшем устройств на время работы с приложением
    // В дальнейшем необходимо доработать
    private val _deviceList = MutableStateFlow(mutableListOf<Device>())

    /** Текущее выбранное устройство */
    private val _device = MutableStateFlow(Device())

    /** Список устройств (компактная модель) */
    private val _deviceCompactList = MutableStateFlow(mutableListOf<DeviceCompact>())

    /** Данные по изображению устройства */
    private val _firebaseImagesData = MutableStateFlow(mutableListOf<FirebaseImageData>())

    val deviceList = _deviceList.asStateFlow()
    val device = _device.asStateFlow()
    val deviceCompactList = _deviceCompactList.asStateFlow()
    val images = _firebaseImagesData.asStateFlow()

    /** Получение списка устройств (компактная модель) */
    suspend fun getDeviceCompact(): Flow<Boolean> =
        flow {
                val devices = deviceRepository.getDeviceCompact()
                devices.forEach { item ->
                    _deviceCompactList.value.add(item.convertToDeviceCompact())
                    _firebaseImagesData.value.add(
                        FirebaseImageData(item.info.model, item.image.size, item.image.extension)
                    )
                }

                emit(true)
            }
            .catch {
                withContext(Dispatchers.Main) { Log.e("DeviceCompactViewModel", "${it.message}") }
                _deviceCompactList.value.add(DeviceCompact())
                _firebaseImagesData.value.add(FirebaseImageData())
                emit(false)
            }

    suspend fun getDeviceByBrandAndUid(brand: String, uid: String): Flow<Boolean> =
        flow {
                val cachedDevice = _deviceList.value.find { device -> device.uid == uid }

                if (cachedDevice !== null) {
                    _device.value = cachedDevice
                } else {
                    val firebaseDevice = deviceRepository.getDeviceByBrandAndUid(brand, uid)
                    val device = firebaseDevice.convertToDevice()
                    _device.value = device
                    _deviceList.value.add(device)
                }

                emit(true)
            }
            .catch {
                withContext(Dispatchers.Main) { Log.e("DeviceCompactViewModel", "${it.message}") }
                emit(false)
            }
}
