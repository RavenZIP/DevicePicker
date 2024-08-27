package com.ravenzip.devicepicker.repositories

import android.util.Log
import com.google.firebase.database.getValue
import com.ravenzip.devicepicker.extensions.functions.convertToClass
import com.ravenzip.devicepicker.model.device.Device
import com.ravenzip.devicepicker.model.device.FirebaseDevice
import com.ravenzip.devicepicker.model.device.compact.DeviceCompact
import com.ravenzip.devicepicker.model.device.compact.FirebaseDeviceCompact
import com.ravenzip.devicepicker.sources.DeviceSources
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
    suspend fun getDeviceByBrandAndUid(brand: String, uid: String): Flow<Device?> =
        flow {
                val response = deviceSources.deviceSourceByPath(brand, uid).get().await()
                val firebaseDevice = response.getValue<FirebaseDevice>()
                val device = firebaseDevice?.convertToDevice()

                emit(device)
            }
            .catch {
                withContext(Dispatchers.Main) { Log.e("DeviceCompactViewModel", "${it.message}") }

                emit(null)
            }

    suspend fun getDeviceCompactList(): Flow<List<DeviceCompact>> =
        flow {
                val response = deviceSources.deviceCompactSource().get().await().children
                val listOfFirebaseDeviceCompact = response.convertToClass<FirebaseDeviceCompact>()
                val listOfDeviceCompact =
                    listOfFirebaseDeviceCompact.map { device -> device.convertToDeviceCompact() }

                emit(listOfDeviceCompact)
            }
            .catch {
                withContext(Dispatchers.Main) { Log.e("getDeviceCompactList", "${it.message}") }

                emit(listOf())
            }
}
