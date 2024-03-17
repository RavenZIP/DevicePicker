package com.ravenzip.devicepicker.services

import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import com.google.firebase.storage.storage
import com.ravenzip.devicepicker.data.device.FirebaseImage
import com.ravenzip.devicepicker.data.device.compact.DeviceCompact
import com.ravenzip.devicepicker.data.device.compact.FirebaseDeviceCompact
import com.ravenzip.devicepicker.extensions.functions.convertToImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

private val database_t = FirebaseDatabase.getInstance().getReference("Devices")

private val storage = Firebase.storage

val devices: MutableList<DeviceCompact> = mutableListOf()

/** Получить список устройств */
suspend fun getDevicesList() {
    val itemsList = database_t.child("Redmi").get().await().children
    itemsList.forEach {
        val item = it.getValue<FirebaseDeviceCompact>()
        if (item !== null) {
            val image = getImage(item.model, item.imageData)
            val device = item.convertToDeviceCompact(image)
            devices.add(device)
        }
    }
}

suspend fun getImage(fileName: String, imageData: FirebaseImage): ImageBitmap {
    return try {
        val image =
            Firebase.storage
                .getReference(fileName + imageData.extension)
                .getBytes(imageData.size)
                .await()
        image.convertToImageBitmap()
    } catch (e: Exception) {
        withContext(Dispatchers.Main) { Log.d("getImage", "${e.message}") }
        ImageBitmap(724, 1500)
    }
}

/** Получить все изображения из корневой директории */
suspend fun getAllImages() {
    val test1 = storage.reference.listAll().await()
    test1.items.forEach { Log.d("", it.name) }
}

/** Получить все изображения из указанной директории */
suspend fun getAllImages(reference: String) {
    val test1 = storage.getReference(reference).listAll().await()
    Log.d("", test1.toString())
}
