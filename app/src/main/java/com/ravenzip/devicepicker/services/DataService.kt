package com.ravenzip.devicepicker.services

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import com.google.firebase.storage.storage
import com.ravenzip.devicepicker.data.Device
import kotlinx.coroutines.tasks.await

// private val database =
//    FirebaseFirestore.getInstance()
//        .collection("devices")
//        .document("smartphones")
//        .collection("Redmi")

private val database_t = FirebaseDatabase.getInstance().getReference("Devices")

private val storage = Firebase.storage

val devices: MutableList<Device> = mutableListOf()

lateinit var image: ByteArray

suspend fun hello() {
    //    val test = database.get().await().documents
    //    test.forEach {
    //        val item = it.toObject<Device>()
    //        if (item !== null) {
    //            devices.add(item)
    //            Log.d("test", item.toString())
    //        }
    //    }

    val test2 = database_t.child("Redmi").get().await().children

    test2.forEach {
        Log.d("TEST", it.toString())
        val item = it.getValue<Device>()
        if (item !== null) {
            devices.add(item)
            Log.d("test", item.toString())
        }
    }

    if (devices.isEmpty()) {
        devices.add(Device())
    }
    getAllImages()
    image = Firebase.storage.getReference("Redmi Note 7.webp").getBytes(1024 * 1024).await()
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
