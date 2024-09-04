package com.ravenzip.devicepicker.repositories

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import com.google.firebase.auth.FirebaseUser
import com.ravenzip.devicepicker.model.User
import com.ravenzip.devicepicker.model.device.Device
import com.ravenzip.devicepicker.model.device.DeviceQueryParams
import com.ravenzip.devicepicker.model.device.compact.DeviceCompact
import com.ravenzip.devicepicker.model.result.ImageUrlResult
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class SharedRepository
@Inject
constructor(
    private val deviceRepository: DeviceRepository,
    private val imageRepository: ImageRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) {
    /** Все устройства (компактная модель) */
    private val _allDevices = MutableStateFlow(mutableStateListOf<DeviceCompact>())

    /** Кэш устройств на время работы с приложением */
    private val _cachedDevices = MutableStateFlow(listOf<Device>())

    /** Параметры для запроса текущего устройства */
    private val _deviceQueryParams = MutableStateFlow<DeviceQueryParams?>(null)

    /** Данные о пользователе */
    private val _userData = MutableStateFlow(User())

    val allDevices = _allDevices.asStateFlow()
    val deviceQueryParams = _deviceQueryParams.asStateFlow()
    val userData = _userData.asStateFlow()

    /**
     * Текущий пользователь firebase
     *
     * @return [FirebaseUser] или null
     */
    val firebaseUser: FirebaseUser?
        get() = authRepository.firebaseUser

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

    suspend fun getUserData() {
        userRepository.getUserData(firebaseUser?.uid).collect { userData ->
            _userData.update { userData }
        }
    }

    fun getCachedDevice(uid: String): Device? {
        return _cachedDevices.value.find { device -> device.uid == uid }
    }

    fun updateCachedDevices(device: Device) {
        val updatedCachedDevices = _cachedDevices.value + device
        _cachedDevices.update { updatedCachedDevices }
    }

    fun setDeviceQueryParams(uid: String, brand: String, model: String) {
        _deviceQueryParams.update { DeviceQueryParams(uid, brand, model) }
    }

    suspend fun tryToUpdateDeviceHistory(deviceUid: String) {
        if (deviceUid !in _userData.value.deviceHistory) {
            val updatedDeviceHistory = _userData.value.deviceHistory + deviceUid

            // TODO пока что не обрабатываем результат запроса
            userRepository.updateDeviceHistory(firebaseUser?.uid, updatedDeviceHistory)
            _userData.update { _userData.value.copy(deviceHistory = updatedDeviceHistory) }
        }
    }

    fun clearDeviceQueryParams() {
        _deviceQueryParams.update { null }
    }
}
