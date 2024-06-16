package com.ravenzip.devicepicker.repositories

import androidx.compose.ui.graphics.ImageBitmap
import com.ravenzip.devicepicker.extensions.functions.convertToImageBitmap
import com.ravenzip.devicepicker.sources.ImageSources
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await

@Singleton
class ImageRepository @Inject constructor(private val imageSources: ImageSources) {
    suspend fun getImage(path: String, size: Long): ImageBitmap {
        val response = imageSources.imageSourceByPath(path).getBytes(size).await()
        return response.convertToImageBitmap()
    }

    //    suspend fun getImageList(): List<ImageBitmap>{
    //        val response = imageServiceSources.imageSource().listAll().await()
    //    }
    //
    //    suspend fun getImageListByPath(path: String): List<ImageBitmap>{
    //        val response = imageServiceSources.imageSourceByPath(path).listAll().await()
    //    }
}
