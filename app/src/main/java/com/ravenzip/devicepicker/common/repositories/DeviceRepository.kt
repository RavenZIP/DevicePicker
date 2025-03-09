package com.ravenzip.devicepicker.common.repositories

import android.util.Log
import com.google.firebase.database.getValue
import com.ravenzip.devicepicker.common.model.device.Device
import com.ravenzip.devicepicker.common.model.device.DeviceDto
import com.ravenzip.devicepicker.common.model.device.compact.DeviceCompact
import com.ravenzip.devicepicker.common.model.device.compact.DeviceCompactDto
import com.ravenzip.devicepicker.common.sources.DeviceSources
import com.ravenzip.devicepicker.common.utils.extension.convertToClass
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Singleton
class DeviceRepository @Inject constructor(private val deviceSources: DeviceSources) {
    fun getDeviceByUid(uid: String): Flow<Device?> =
        flow {
                val response = deviceSources.deviceSourceByUid(uid).get().await()
                val deviceDto = response.getValue<DeviceDto>()
                val device = deviceDto?.convertToDevice()

                emit(device)
            }
            .catch {
                withContext(Dispatchers.Main) { Log.e("getDeviceByUid", "${it.message}") }

                emit(null)
            }

    fun getDeviceCompactList(): Flow<List<DeviceCompact>> =
        flow {
                val response = deviceSources.deviceCompactSource().get().await().children
                val listOfDeviceCompactDto = response.convertToClass<DeviceCompactDto>()
                val listOfDeviceCompact =
                    listOfDeviceCompactDto.map { device -> device.convertToDeviceCompact() }

                emit(listOfDeviceCompact)
            }
            .catch {
                withContext(Dispatchers.Main) { Log.e("getDeviceCompactList", "${it.message}") }

                emit(listOf())
            }
}
