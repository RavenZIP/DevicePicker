package com.ravenzip.devicepicker.services

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import com.ravenzip.devicepicker.data.device.compact.DeviceCompact
import com.ravenzip.devicepicker.data.device.compact.FirebaseDeviceCompact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class PopularDevicesService : ViewModel() {
    private val databaseRef =
        FirebaseDatabase.getInstance().getReference("PromotionsNew").child("Popular this week")

    private val _devices = MutableStateFlow(mutableListOf<DeviceCompact>())
    val devices = _devices.asStateFlow()

    /** Получить список популярных за неделю устройств */
    suspend fun get() {
        try {
            val startTime = System.currentTimeMillis()
            val popularThisWeek = databaseRef.get().await().children

            popularThisWeek.forEach { data ->
                val item = data.getValue<FirebaseDeviceCompact>()
                if (item !== null) {
                    _devices.value.add(item.convertToDeviceCompact())
                }
            }
            Log.d(
                "PopularDevicesService time (s)",
                ((System.currentTimeMillis() - startTime).toDouble() / 1000).toString()
            )
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { Log.d("getPromotions", "${e.message}") }
            _devices.value.add(DeviceCompact())
        }
    }
}
