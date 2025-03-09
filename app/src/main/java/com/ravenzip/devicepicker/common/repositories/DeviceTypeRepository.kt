package com.ravenzip.devicepicker.common.repositories

import android.util.Log
import com.ravenzip.devicepicker.common.utils.extension.convertToClass
import com.ravenzip.devicepicker.common.sources.DeviceTypeSources
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Singleton
class DeviceTypeRepository @Inject constructor(private val deviceTypeSources: DeviceTypeSources) {
    fun getDeviceTypeList() =
        flow {
                val response = deviceTypeSources.deviceTypeSource().get().await().children
                val deviceTypeList = response.convertToClass<String>()
                emit(deviceTypeList)
            }
            .catch {
                withContext(Dispatchers.Main) { Log.e("getDeviceTypeList", "${it.message}") }
                emit(listOf())
            }
}
