package com.ravenzip.devicepicker.repositories

import com.ravenzip.devicepicker.extensions.functions.convertToClass
import com.ravenzip.devicepicker.sources.DeviceTypeSources
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await

@Singleton
class DeviceTypeRepository @Inject constructor(private val deviceTypeSources: DeviceTypeSources) {
    suspend fun getDeviceTypeList(): List<String> {
        val response = deviceTypeSources.deviceTypeSource().get().await().children
        return response.convertToClass<String>()
    }
}
