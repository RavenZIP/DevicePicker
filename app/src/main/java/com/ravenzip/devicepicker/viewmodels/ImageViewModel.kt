package com.ravenzip.devicepicker.viewmodels

import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import com.ravenzip.devicepicker.data.device.FirebaseImageData
import com.ravenzip.devicepicker.data.result.ImageResult
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
    suspend fun getImage(imageData: FirebaseImageData): Flow<ImageResult<ImageBitmap>> =
        flow {
                val image =
                    imageServiceRepository.getImage(
                        path = imageData.name + imageData.extension,
                        size = imageData.size
                    )
                emit(ImageResult(value = image, imageName = imageData.name))
            }
            .catch {
                withContext(Dispatchers.Main) { Log.d("getImage", "${it.message}") }
                emit(
                    ImageResult(
                        value = ImageBitmap(width = 150, height = 150),
                        imageName = imageData.name
                    )
                )
            }

    /** Получение нескольких изображений */
    suspend fun getImages(
        imagesData: List<FirebaseImageData>
    ): Flow<Flow<ImageResult<ImageBitmap>>> {
        return imagesData.map { image -> getImage(image) }.asFlow()
    }
}
