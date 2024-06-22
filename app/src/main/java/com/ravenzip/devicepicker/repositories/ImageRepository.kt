package com.ravenzip.devicepicker.repositories

import com.ravenzip.devicepicker.sources.ImageSources
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await

@Singleton
class ImageRepository @Inject constructor(private val imageSources: ImageSources) {
    /** Получение урла изображения устройства из указанной директории */
    suspend fun getMainImageUrlByFolder(brand: String, model: String): String {
        val response =
            imageSources
                .imageSourceByPath(brand = brand, model = model)
                .child("$model.webp")
                .downloadUrl
                .await()

        return response.toString()
    }

    /** Получить урлы всех изображений из указанной директории */
    suspend fun getImageListByFolder(brand: String, model: String): List<String> {
        val response =
            imageSources.imageSourceByPath(brand = brand, model = model).listAll().await()

        return response.items.map { it.downloadUrl.await().toString() }
    }
}
