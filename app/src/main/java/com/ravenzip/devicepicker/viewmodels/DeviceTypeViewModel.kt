package com.ravenzip.devicepicker.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.ravenzip.devicepicker.repositories.DeviceTypeRepository
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
class DeviceTypeViewModel
@Inject
constructor(private val deviceTypeRepository: DeviceTypeRepository) : ViewModel() {
    private val _deviceTypeList = MutableStateFlow(mutableListOf<String>())

    val deviceTypeList = _deviceTypeList.asStateFlow()

    suspend fun getDeviceTypeList(): Flow<Boolean> =
        flow {
                val deviceTypeList = deviceTypeRepository.getDeviceTypeList()
                _deviceTypeList.value.addAll(deviceTypeList)
                emit(true)
            }
            .catch {
                withContext(Dispatchers.Main) { Log.e("DeviceTypeViewModel", "${it.message}") }
                emit(false)
            }
}
