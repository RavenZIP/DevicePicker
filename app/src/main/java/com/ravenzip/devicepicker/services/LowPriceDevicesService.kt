package com.ravenzip.devicepicker.services

import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import com.ravenzip.devicepicker.data.device.FirebaseImageData
import com.ravenzip.devicepicker.data.device.compact.DeviceCompact
import com.ravenzip.devicepicker.data.device.compact.FirebaseDeviceCompact
import com.ravenzip.devicepicker.data.result.ImageResult
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
class LowPriceDevicesService @Inject constructor() : ViewModel() {
    private val databaseRef =
        FirebaseDatabase.getInstance().getReference("PromotionsNew").child("Low price")

    private val _devices = MutableStateFlow(mutableListOf<DeviceCompact>())
    private val _firebaseImagesData = MutableStateFlow(mutableListOf<FirebaseImageData>())

    val devices = _devices.asStateFlow()
    val images = _firebaseImagesData.asStateFlow()

    /** Получить список устройств с низкой ценой */
    suspend fun get(): Flow<Boolean> =
        flow {
                Log.d("LowPriceService", "GET")
                val startTime = System.currentTimeMillis()
                val lowPrice = databaseRef.get().await().children

                lowPrice.forEach { data ->
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
                Log.d(
                    "LowPriceService",
                    ((System.currentTimeMillis() - startTime).toDouble() / 1000).toString() +
                        " сек."
                )
                Log.d("LowPriceService", "END")
                emit(true)
            }
            .catch {
                withContext(Dispatchers.Main) { Log.e("LowPriceService", "${it.message}") }
                _devices.value.add(DeviceCompact())
                _firebaseImagesData.value.add(FirebaseImageData())
                emit(false)
            }

    fun setImage(image: ImageResult<ImageBitmap>) {
        val index = _devices.value.indexOfFirst { it.model === image.imageName }
        _devices.value[index] = _devices.value[index].copy(image = image.value)
    }
}
