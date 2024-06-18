package com.ravenzip.devicepicker.repositories

import com.google.firebase.database.getValue
import com.ravenzip.devicepicker.data.device.compact.FirebaseDevice
import com.ravenzip.devicepicker.data.device.compact.FirebaseDeviceCompact
import com.ravenzip.devicepicker.extensions.functions.convertToClass
import com.ravenzip.devicepicker.sources.DeviceSources
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await

@Singleton
class DeviceRepository @Inject constructor(private val deviceSources: DeviceSources) {
    suspend fun getDeviceByBrandAndUid(brand: String, uid: String): FirebaseDevice {
        val response = deviceSources.deviceSourceByPath(brand, uid).get().await()
        val convertedData = response.getValue<FirebaseDevice>()
        return convertedData ?: FirebaseDevice()
    }

    suspend fun getDeviceCompact(): List<FirebaseDeviceCompact> {
        val response = deviceSources.deviceCompactSource().get().await().children
        return response.convertToClass<FirebaseDeviceCompact>()
    }
}
