package com.ravenzip.devicepicker.repositories

import android.util.Log
import com.ravenzip.devicepicker.model.device.compact.DeviceCompact
import com.ravenzip.devicepicker.model.result.ImageUrlResult
import com.ravenzip.devicepicker.sources.ImageSources
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Singleton
class ImageRepository @Inject constructor(private val imageSources: ImageSources) {
    /** Получение урла изображения устройства из указанной директории */
    private suspend fun getMainImageUrlByFolder(brand: String, model: String): String {
        val response =
            imageSources
                .imageSourceByPath(brand = brand, model = model)
                .child("$model.webp")
                .downloadUrl
                .await()

        return response.toString()
    }

    /** Получить урлы всех изображений из указанной директории */
    private suspend fun getImageListByFolder(brand: String, model: String): List<String> {
        val response =
            imageSources.imageSourceByPath(brand = brand, model = model).listAll().await()

        return response.items.map { it.downloadUrl.await().toString() }
    }

    /** Получить урл основного изображения конкретного устройства */
    private fun getImageUrl(
        deviceUid: String,
        brand: String,
        model: String,
    ): Flow<ImageUrlResult<String>> =
        flow {
                val imageUrl = getMainImageUrlByFolder(brand, model)
                emit(ImageUrlResult(value = imageUrl, deviceUid = deviceUid))
            }
            .catch {
                withContext(Dispatchers.Main) { Log.d("getImageUrl", "${it.message}") }
                emit(ImageUrlResult(value = "", deviceUid = deviceUid))
            }

    /** Получение основного урла изображения для списка устройств */
    fun getImageUrls(deviceCompactList: List<DeviceCompact>): Flow<Flow<ImageUrlResult<String>>> {
        return deviceCompactList
            .map { device -> getImageUrl(device.uid, device.brand, device.model) }
            .asFlow()
    }

    /** Получение нескольких урлов изображений для конкретного устройства */
    fun getImageUrls(brand: String, model: String): Flow<List<String>> =
        flow {
                val imageUrls = getImageListByFolder(brand, model)
                emit(imageUrls)
            }
            .catch {
                withContext(Dispatchers.Main) { Log.d("getImageUrls", "${it.message}") }
                emit(listOf())
            }
}
