package com.ravenzip.devicepicker.services

import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import com.ravenzip.devicepicker.data.device.FirebaseImageData
import com.ravenzip.devicepicker.extensions.functions.convertToImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ImageService : ViewModel() {
    private val storage = Firebase.storage

    /** Получить основное изображение конкретного товара */
    suspend fun getImage(fileName: String, imageData: FirebaseImageData): ImageBitmap {
        return try {
            val image =
                storage
                    .getReference(fileName + imageData.extension)
                    .getBytes(imageData.size)
                    .await()
            image.convertToImageBitmap()
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { Log.d("getImage", "${e.message}") }
            ImageBitmap(150, 150)
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
