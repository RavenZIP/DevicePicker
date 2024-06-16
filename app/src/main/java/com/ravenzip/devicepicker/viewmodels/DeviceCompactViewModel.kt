package com.ravenzip.devicepicker.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.ravenzip.devicepicker.data.device.FirebaseImageData
import com.ravenzip.devicepicker.data.device.compact.DeviceCompact
import com.ravenzip.devicepicker.repositories.DeviceCompactRepository
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
class DeviceCompactViewModel
@Inject
constructor(private val deviceCompactRepository: DeviceCompactRepository) : ViewModel() {
    private val _devices = MutableStateFlow(mutableListOf<DeviceCompact>())
    private val _firebaseImagesData = MutableStateFlow(mutableListOf<FirebaseImageData>())

    val devices = _devices.asStateFlow()
    val images = _firebaseImagesData.asStateFlow()

    suspend fun getDevices(): Flow<Boolean> =
        flow {
                val devices = deviceCompactRepository.getDevices()
                devices.forEach { item ->
                    _devices.value.add(item.convertToDeviceCompact())
                    _firebaseImagesData.value.add(
                        FirebaseImageData(item.info.model, item.image.size, item.image.extension)
                    )
                }

                emit(true)
            }
            .catch {
                withContext(Dispatchers.Main) { Log.e("DeviceCompactViewModel", "${it.message}") }
                _devices.value.add(DeviceCompact())
                _firebaseImagesData.value.add(FirebaseImageData())
                emit(false)
            }
}
