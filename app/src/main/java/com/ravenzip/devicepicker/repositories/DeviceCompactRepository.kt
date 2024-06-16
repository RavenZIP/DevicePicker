package com.ravenzip.devicepicker.repositories

import com.ravenzip.devicepicker.data.device.compact.FirebaseDeviceCompact
import com.ravenzip.devicepicker.extensions.functions.convertToClass
import com.ravenzip.devicepicker.sources.DeviceCompactSources
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await

@Singleton
class DeviceCompactRepository
@Inject
constructor(private val deviceCompactSources: DeviceCompactSources) {
    suspend fun getDevices(): List<FirebaseDeviceCompact> {
        val response = deviceCompactSources.deviceCompactSource().get().await().children
        return response.convertToClass<FirebaseDeviceCompact>()
    }
}
