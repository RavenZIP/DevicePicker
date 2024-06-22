package com.ravenzip.devicepicker.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.ravenzip.devicepicker.model.device.compact.DeviceCompact
import com.ravenzip.devicepicker.model.result.ImageUrlResult
import com.ravenzip.devicepicker.repositories.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

@HiltViewModel
class ImageViewModel @Inject constructor(private val imageServiceRepository: ImageRepository) :
    ViewModel() {

    /** Получить основное изображение конкретного товара */
    suspend fun getImageUrl(
        deviceUid: String,
        brand: String,
        model: String
    ): Flow<ImageUrlResult<String>> =
        flow {
                val imageUrl = imageServiceRepository.getMainImageUrlByFolder(brand, model)
                emit(ImageUrlResult(value = imageUrl, deviceUid = deviceUid))
            }
            .catch {
                withContext(Dispatchers.Main) { Log.d("getImageUrl", "${it.message}") }
                emit(ImageUrlResult(value = "", deviceUid = deviceUid))
            }

    /** Получение нескольких изображений */
    suspend fun getImageUrls(
        deviceCompactList: List<DeviceCompact>
    ): Flow<Flow<ImageUrlResult<String>>> {
        return deviceCompactList
            .map { device -> getImageUrl(device.uid, device.brand, device.model) }
            .asFlow()
    }
}
