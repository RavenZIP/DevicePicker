package com.ravenzip.devicepicker.services

import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import com.ravenzip.devicepicker.data.device.compact.DeviceCompact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DataService : ViewModel() {
    private val databaseInstance = FirebaseDatabase.getInstance()

    private val _devices = MutableStateFlow(mutableListOf<DeviceCompact>())
    private val _similarDevices = MutableStateFlow(mutableListOf<DeviceCompact>())
    private val _companyBestDevices = MutableStateFlow(mutableListOf<DeviceCompact>())
    private val _unknown = MutableStateFlow(mutableListOf<DeviceCompact>())
    private val _isLoading = MutableStateFlow(false)

    val devices = _devices.asStateFlow()
    val similarDevices = _similarDevices.asStateFlow()
    val companyBestDevices = _companyBestDevices.asStateFlow()
    val unknown = _unknown.asStateFlow()
    val isLoading = _isLoading.asStateFlow()

    /** Получить список устройств на основе недавно просмотренных */
    //    suspend fun getSimilarDevices() {
    //        try {
    //            val startTime = System.currentTimeMillis()
    //            val similarDevicesRef =
    //                databaseInstance.getReference("PromotionsNew").child("Similar devices")
    //            val similarDevices = similarDevicesRef.get().await().children
    //
    //            similarDevices.forEach { data ->
    //                val item = data.getValue<FirebaseDeviceCompact>()
    //                if (item !== null) {
    //                    val image = getImage(item.model, item.imageData)
    //                    val device = item.convertToDeviceCompact(image)
    //                    _similarDevices.value.add(device)
    //                }
    //            }
    //            Log.d(
    //                "getSimilarDevices time s",
    //                ((System.currentTimeMillis() - startTime).toDouble() / 1000).toString()
    //            )
    //        } catch (e: Exception) {
    //            withContext(Dispatchers.Main) { Log.d("getPromotions", "${e.message}") }
    //            _similarDevices.value.add(DeviceCompact())
    //        }
    //    }

    /** Получить список лучших устройств компании */
    //    suspend fun getCompanyBestDevices() {
    //        try {
    //            val startTime = System.currentTimeMillis()
    //            val companyBestDevicesRef =
    //                databaseInstance.getReference("PromotionsNew").child("Redmi: The best
    // devices")
    //            val companyBestDevices = companyBestDevicesRef.get().await().children
    //
    //            companyBestDevices.forEach { data ->
    //                val item = data.getValue<FirebaseDeviceCompact>()
    //                if (item !== null) {
    //                    val image = getImage(item.model, item.imageData)
    //                    val device = item.convertToDeviceCompact(image)
    //                    _companyBestDevices.value.add(device)
    //                }
    //            }
    //            Log.d(
    //                "getCompanyBestDevices time s",
    //                ((System.currentTimeMillis() - startTime).toDouble() / 1000).toString()
    //            )
    //        } catch (e: Exception) {
    //            withContext(Dispatchers.Main) { Log.d("getPromotions", "${e.message}") }
    //            _companyBestDevices.value.add(DeviceCompact())
    //        }
    //    }

    /** Получить список устройств... */
    //    suspend fun getUnknown() {
    //        try {
    //            val startTime = System.currentTimeMillis()
    //            val unknownRef = databaseInstance.getReference("PromotionsNew").child("Title")
    //            val unknown = unknownRef.get().await().children
    //
    //            unknown.forEach { data ->
    //                val item = data.getValue<FirebaseDeviceCompact>()
    //                if (item !== null) {
    //                    val image = getImage(item.model, item.imageData)
    //                    val device = item.convertToDeviceCompact(image)
    //                    _unknown.value.add(device)
    //                }
    //            }
    //            Log.d(
    //                "getUnknown time s",
    //                ((System.currentTimeMillis() - startTime).toDouble() / 1000).toString()
    //            )
    //        } catch (e: Exception) {
    //            withContext(Dispatchers.Main) { Log.d("getPromotions", "${e.message}") }
    //            _unknown.value.add(DeviceCompact())
    //        }
    //    }

    /** Получить список устройств */
    //    suspend fun getDevices() {
    //        val database = databaseInstance.getReference("Devices")
    //        val itemsList = database.child("Redmi").get().await().children
    //
    //        itemsList.forEach {
    //            val item = it.getValue<FirebaseDeviceCompact>()
    //            if (item !== null) {
    //                val image = getImage(item.model, item.imageData)
    //                val device = item.convertToDeviceCompact(image)
    //                _devices.value.add(device)
    //            }
    //        }
    //    }
}
