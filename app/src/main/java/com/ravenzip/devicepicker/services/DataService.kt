package com.ravenzip.devicepicker.services

import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import com.google.firebase.storage.storage
import com.ravenzip.devicepicker.data.device.FirebaseImageData
import com.ravenzip.devicepicker.data.device.compact.DeviceCompact
import com.ravenzip.devicepicker.data.device.compact.FirebaseDeviceCompact
import com.ravenzip.devicepicker.extensions.functions.convertToImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DataService : ViewModel() {
    private val databaseInstance = FirebaseDatabase.getInstance()
    private val storage = Firebase.storage
    private val _devices = MutableStateFlow(mutableListOf<DeviceCompact>())
    val devices = _devices.asStateFlow()

    /** Получить список устройств, по которым есть акции и специальные предложения */
    suspend fun getPromotions() {
        try {
            val database = databaseInstance.getReference("Promotions")
            val itemsList = database.get().await().children

            itemsList.forEach {
                val item = it.getValue<FirebaseDeviceCompact>()
                if (item !== null) {
                    val image = getImage(item.model, item.imageData)
                    val device = item.convertToDeviceCompact(image)
                    _devices.value.add(device)
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { Log.d("getPromotions", "${e.message}") }
            _devices.value.add(DeviceCompact())
        }
    }

    /** Получить основное изображение конкретного товара */
    private suspend fun getImage(fileName: String, imageData: FirebaseImageData): ImageBitmap {
        return try {
            val image =
                storage
                    .getReference(fileName + imageData.extension)
                    .getBytes(imageData.size)
                    .await()
            image.convertToImageBitmap()
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { Log.d("getImage", "${e.message}") }
            ImageBitmap(724, 1500)
        }
    }

    /** Получить список устройств */
    suspend fun getDevices() {
        val database = databaseInstance.getReference("Devices")
        val itemsList = database.child("Redmi").get().await().children

        itemsList.forEach {
            val item = it.getValue<FirebaseDeviceCompact>()
            if (item !== null) {
                val image = getImage(item.model, item.imageData)
                val device = item.convertToDeviceCompact(image)
                _devices.value.add(device)
            }
        }
    }

    /** Получить все изображения из корневой директории */
    suspend fun getAllImages() {
        val test1 = storage.reference.listAll().await()
    }

    /** Получить все изображения из указанной директории */
    suspend fun getAllImages(reference: String) {
        val test1 = storage.getReference(reference).listAll().await()
    }
}
