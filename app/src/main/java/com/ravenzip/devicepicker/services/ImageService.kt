package com.ravenzip.devicepicker.services

import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import com.ravenzip.devicepicker.data.device.FirebaseImageData
import com.ravenzip.devicepicker.data.result.ImageResult
import com.ravenzip.devicepicker.extensions.functions.convertToImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ImageService : ViewModel() {
    private val storage = Firebase.storage

    /** Получить основное изображение конкретного товара */
    suspend fun getImage(imageData: FirebaseImageData): Flow<ImageResult<ImageBitmap>> =
        flow {
                Log.d("ImageService", "GET")
                val startTime = System.currentTimeMillis()
                val image =
                    storage
                        .getReference(imageData.name + imageData.extension)
                        .getBytes(imageData.size)
                        .await()

                Log.d(
                    "ImageService",
                    ((System.currentTimeMillis() - startTime).toDouble() / 1000).toString() +
                        " сек."
                )
                Log.d("ImageService", "END")
                emit(ImageResult(image.convertToImageBitmap(), imageName = imageData.name))
            }
            .catch {
                withContext(Dispatchers.Main) { Log.d("getImage", "${it.message}") }
                emit(ImageResult(value = ImageBitmap(150, 150), imageName = imageData.name))
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
