package com.ravenzip.devicepicker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.model.device.Device
import com.ravenzip.devicepicker.model.result.Result
import com.ravenzip.devicepicker.repositories.DeviceRepository
import com.ravenzip.devicepicker.repositories.ImageRepository
import com.ravenzip.devicepicker.repositories.SharedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

@HiltViewModel
class DeviceInfoViewModel
@Inject
constructor(
    private val deviceRepository: DeviceRepository,
    private val imageRepository: ImageRepository,
    private val sharedRepository: SharedRepository,
) : ViewModel() {
    /** Текущее выбранное устройство */
    private val _device = MutableStateFlow<Result<Device?>>(Result.default())

    val device = _device.asStateFlow()

    init {
        viewModelScope.launch {
            sharedRepository.deviceQueryParams.collect { params ->
                _device.update { Result.loading() }
                val cachedDevice = sharedRepository.getCachedDevice(params!!.uid)

                if (cachedDevice == null) {
                    getDeviceByBrandAndUid(params.uid, params.brand, params.model)
                } else {
                    _device.update { Result.success(cachedDevice) }
                }

                sharedRepository.tryToUpdateDeviceHistory(params.uid)
            }
        }
    }

    /** Получение устройства по бренду и уникальному идентификатору */
    private suspend fun getDeviceByBrandAndUid(uid: String, brand: String, model: String) {
        deviceRepository
            .getDeviceByBrandAndUid(brand, uid)
            .zip(imageRepository.getImageUrls(brand, model)) { device, imageUrls ->
                if (device != null) {
                    val deviceWithImageUrls = device.copy(imageUrls = imageUrls)

                    _device.update { Result.success(deviceWithImageUrls) }
                    sharedRepository.updateCachedDevices(deviceWithImageUrls)
                } else {
                    val errorMessage = "При выполении запроса произошла ошибка"
                    _device.update { Result.error(errorMessage = errorMessage) }
                }
            }
            .collect {}
    }
}
