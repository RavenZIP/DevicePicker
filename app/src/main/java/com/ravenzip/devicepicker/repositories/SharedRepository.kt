package com.ravenzip.devicepicker.repositories

import com.ravenzip.devicepicker.model.User
import com.ravenzip.devicepicker.model.User.Companion.fullName
import com.ravenzip.devicepicker.model.device.Device
import com.ravenzip.devicepicker.model.device.compact.DeviceCompact
import com.ravenzip.devicepicker.model.result.ImageUrlResult
import com.ravenzip.kotlinflowextended.models.FlowNotification
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class SharedRepository
@Inject
constructor(
    private val deviceRepository: DeviceRepository,
    private val imageRepository: ImageRepository,
    private val userRepository: UserRepository,
) {
    /** Все устройства (компактная модель) */
    private val _allDevices = MutableStateFlow(listOf<DeviceCompact>())

    /** Кэш устройств на время работы с приложением */
    private val _cachedDevices = MutableStateFlow(listOf<Device>())

    /** Данные о пользователе */
    private val _userData = MutableStateFlow(User())

    val allDevices = _allDevices.asStateFlow()
    val userData = _userData.asStateFlow()

    val deviceHistory = _userData.map { userData -> userData.deviceHistory }
    val favourites = _userData.map { userData -> userData.favourites }
    val compares = _userData.map { userData -> userData.compares }
    val userFullName = _userData.map { userData -> userData.fullName }

    /** Получение списка устройств и их изображений (компактная модель) */
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getDeviceCompactList() {
        deviceRepository
            .getDeviceCompactList()
            .onEach { devices -> _allDevices.update { devices.toList() } }
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

            _allDevices.update { updatedAllDevices.toList() }
        }
    }

    fun loadUserData(): Flow<FlowNotification<User>> {
        return userRepository.getUserData().onEach { flowNotification ->
            if (flowNotification is FlowNotification.Next) {
                _userData.update { flowNotification.value }
            }
        }
    }

    fun getCachedDevice(uid: String): Device? {
        return _cachedDevices.value.find { device -> device.uid == uid }
    }

    fun updateCachedDevices(device: Device) {
        val updatedCachedDevices = _cachedDevices.value + device
        _cachedDevices.update { updatedCachedDevices }
    }

    suspend fun tryToUpdateDeviceHistory(deviceUid: String) {
        if (deviceUid !in _userData.value.deviceHistory) {
            val updatedDeviceHistory = _userData.value.deviceHistory + deviceUid
            val updateResult = userRepository.updateDeviceHistory(updatedDeviceHistory)

            if (updateResult) {
                _userData.update { _userData.value.copy(deviceHistory = updatedDeviceHistory) }
            }
        }
    }

    suspend fun tryToUpdateFavourites(deviceUid: String) {
        val updatedFavourites =
            if (isFavouriteDevice(deviceUid)) {
                _userData.value.favourites - deviceUid
            } else {
                _userData.value.favourites + deviceUid
            }

        val updateResult = userRepository.updateFavourites(updatedFavourites)

        if (updateResult) {
            _userData.update { _userData.value.copy(favourites = updatedFavourites) }
        }
    }

    suspend fun tryToUpdateCompares(deviceUid: String) {
        val updatedCompares =
            if (isComparedDevice(deviceUid)) {
                _userData.value.compares - deviceUid
            } else {
                _userData.value.compares + deviceUid
            }

        val updateResult = userRepository.updateCompares(updatedCompares)

        if (updateResult) {
            _userData.update { _userData.value.copy(compares = updatedCompares) }
        }
    }

    private fun isFavouriteDevice(deviceUid: String): Boolean {
        return deviceUid in _userData.value.favourites
    }

    private fun isComparedDevice(deviceUid: String): Boolean {
        return deviceUid in _userData.value.compares
    }
}
