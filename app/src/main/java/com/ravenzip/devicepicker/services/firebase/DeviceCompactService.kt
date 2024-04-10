package com.ravenzip.devicepicker.services.firebase

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import com.ravenzip.devicepicker.data.device.FirebaseImageData
import com.ravenzip.devicepicker.data.device.compact.DeviceCompact
import com.ravenzip.devicepicker.data.device.compact.FirebaseDeviceCompact
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@HiltViewModel
class DeviceCompactService @Inject constructor() : ViewModel() {
    private val databaseRef = FirebaseDatabase.getInstance().getReference("DeviceCompact")

    private val _devices = MutableStateFlow(mutableListOf<DeviceCompact>())
    private val _firebaseImagesData = MutableStateFlow(mutableListOf<FirebaseImageData>())

    val devices = _devices.asStateFlow()
    val images = _firebaseImagesData.asStateFlow()

    suspend fun get(): Flow<Boolean> =
        flow {
                Log.d("DeviceCompactService", "GET")
                val startTime = System.currentTimeMillis()
                val response = databaseRef.get().await().children

                response.forEach { data ->
                    val item = data.getValue<FirebaseDeviceCompact>()
                    if (item !== null) {
                        _devices.value.add(item.convertToDeviceCompact())
                        _firebaseImagesData.value.add(
                            FirebaseImageData(
                                item.info.model,
                                item.image.size,
                                item.image.extension
                            )
                        )
                    }
                }

                val endTime = (System.currentTimeMillis() - startTime).toDouble() / 1000
                Log.d("DeviceCompactService", "$endTime сек.")
                Log.d("DeviceCompactService", "END")
                emit(true)
            }
            .catch {
                withContext(Dispatchers.Main) { Log.e("DeviceCompactService", "${it.message}") }
                _devices.value.add(DeviceCompact())
                _firebaseImagesData.value.add(FirebaseImageData())
                emit(false)
            }
}
