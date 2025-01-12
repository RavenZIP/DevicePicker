package com.ravenzip.devicepicker.viewmodels.home

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.extensions.functions.showMessage
import com.ravenzip.devicepicker.model.ButtonData
import com.ravenzip.devicepicker.model.Feedback
import com.ravenzip.devicepicker.model.device.Device
import com.ravenzip.devicepicker.model.device.Device.Companion.createDeviceTitle
import com.ravenzip.devicepicker.model.device.Device.Companion.createShortTags
import com.ravenzip.devicepicker.model.device.Device.Companion.createTags
import com.ravenzip.devicepicker.model.device.compact.DeviceSpecifications.Companion.toMap
import com.ravenzip.devicepicker.repositories.DeviceRepository
import com.ravenzip.devicepicker.repositories.ImageRepository
import com.ravenzip.devicepicker.repositories.SharedRepository
import com.ravenzip.devicepicker.state.UiState
import com.ravenzip.workshop.data.appbar.AppBarItem
import com.ravenzip.workshop.data.icon.Icon
import com.ravenzip.workshop.data.icon.IconConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DeviceInfoViewModel
@Inject
constructor(
    private val deviceRepository: DeviceRepository,
    private val imageRepository: ImageRepository,
    private val sharedRepository: SharedRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _updateFavourites = MutableSharedFlow<Unit>()

    private val _findCachedDeviceComplete =
        savedStateHandle.getStateFlow("uid", "").map { uid ->
            sharedRepository.getCachedDevice(uid)
        }

    private val _findCachedDeviceSuccess = _findCachedDeviceComplete.filterNotNull()

    private val _findCachedDeviceError =
        _findCachedDeviceComplete.filter { device -> device == null }

    private val _loadDeviceComplete =
        _findCachedDeviceError.flatMapLatest {
            val uid = savedStateHandle["uid"] ?: ""
            return@flatMapLatest deviceRepository.getDeviceByUid(uid)
        }

    private val _loadDeviceSuccess =
        _loadDeviceComplete
            .filterNotNull()
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _loadDeviceError = _loadDeviceComplete.filter { device -> device == null }

    private val _loadDeviceImagesComplete =
        _loadDeviceSuccess
            .flatMapLatest { device ->
                imageRepository.getImageUrls(
                    device.specifications.baseInfo.model.split(' ')[0],
                    device.specifications.baseInfo.model,
                )
            }
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _loadDeviceImagesSuccess =
        _loadDeviceImagesComplete.filter { imageUrls -> imageUrls.isNotEmpty() }

    private val _loadDeviceImagesError =
        _loadDeviceImagesComplete.filter { imageUrls -> imageUrls.isEmpty() }

    private val _deviceWithImageUrls =
        _loadDeviceSuccess
            .zip(_loadDeviceImagesSuccess) { device, imageUrls ->
                device.copy(imageUrls = imageUrls)
            }
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    val device =
        merge(
                merge(_deviceWithImageUrls, _findCachedDeviceSuccess).map { device ->
                    UiState.Success(device)
                },
                merge(_loadDeviceError, _loadDeviceImagesError).map {
                    UiState.Error("При выполении запроса произошла ошибка")
                },
            )
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = UiState.Loading("Загрузка..."),
            )

    /** Устройство успешно получено */
    private val _deviceStateIsSuccess =
        device
            .filterIsInstance<UiState.Success<Device>>()
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    val snackBarHostState = SnackbarHostState()

    val title =
        _deviceStateIsSuccess
            .map { deviceData -> deviceData.data.createDeviceTitle() }
            .stateIn(scope = viewModelScope, started = SharingStarted.Lazily, initialValue = "")

    val specifications =
        _deviceStateIsSuccess
            .map { deviceData -> deviceData.data.specifications.toMap() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = mapOf(),
            )

    val specificationsKeys =
        specifications
            .map { specifications -> specifications.keys.toList() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = listOf(),
            )

    val shortTags =
        _deviceStateIsSuccess
            .map { deviceData -> deviceData.data.createShortTags() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = listOf(),
            )

    val tags =
        _deviceStateIsSuccess
            .map { deviceData -> deviceData.data.createTags() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = listOf(),
            )

    val feedbacks =
        _deviceStateIsSuccess
            .map { deviceData -> generateFeedbackList(deviceData.data.feedback) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = listOf(),
            )

    private val _isFavourite =
        _deviceStateIsSuccess
            .flatMapLatest { deviceState ->
                sharedRepository.favourites.map { favourites -> deviceState.data.uid in favourites }
            }
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    //    private val _isCompare =
    //        _deviceStateIsSuccess
    //            .combine(sharedRepository.compares) { device, compares -> device.data.uid in
    // compares }
    //            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _updateFavouritesComplete =
        _updateFavourites.flatMapLatest {
            _deviceStateIsSuccess.flatMapLatest { deviceState ->
                flowOf(sharedRepository.tryToUpdateFavourites(deviceState.data.uid))
            }
        }

    private val _isFavouriteHasBeenChanged =
        _updateFavouritesComplete
            .flatMapLatest { _isFavourite }
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val _deviceAddedToFavourites =
        _isFavouriteHasBeenChanged.filter { isFavourite -> isFavourite }

    private val _deviceDeletedFromFavourites =
        _isFavouriteHasBeenChanged.filter { isFavourite -> !isFavourite }

    val topAppBarButtons =
        _isFavourite
            .map { isFavourite -> generateTopAppBarItems(isFavourite) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = listOf(),
            )

    init {
        viewModelScope.launch {
            _deviceWithImageUrls.collect { device -> sharedRepository.updateCachedDevices(device) }
        }

        viewModelScope.launch {
            merge(
                    _deviceAddedToFavourites.map { "Устройство добавлено в избранное" },
                    _deviceDeletedFromFavourites.map { "Устройство удалено из избранного" },
                )
                .collect { message -> snackBarHostState.showMessage(message) }
        }
    }

    private fun generateFeedbackList(feedback: Feedback): List<ButtonData> {
        val rating =
            ButtonData(
                iconId = R.drawable.i_medal,
                value = feedback.rating.toString(),
                text = "Оценка",
                onClick = {},
            )

        val reviewsCount =
            ButtonData(
                iconId = R.drawable.i_comment,
                value = feedback.reviewsCount.toString(),
                text = "Отзывы",
                onClick = {},
            )

        val questionsCount =
            ButtonData(
                iconId = R.drawable.i_question,
                value = feedback.questionsCount.toString(),
                text = "Вопросы",
                onClick = {},
            )

        return listOf(rating, reviewsCount, questionsCount)
    }

    private fun generateTopAppBarItems(isFavourite: Boolean): List<AppBarItem> {
        val favouriteButton =
            AppBarItem(
                icon =
                    Icon.ResourceIcon(
                        if (isFavourite) R.drawable.i_heart_filled else R.drawable.i_heart
                    ),
                iconConfig = IconConfig.Small,
                onClick = { viewModelScope.launch { _updateFavourites.emit(Unit) } },
            )

        val compareButton =
            AppBarItem(
                icon = Icon.ResourceIcon(R.drawable.i_compare),
                iconConfig = IconConfig.Small,
                onClick = {},
            )

        return listOf(favouriteButton, compareButton)
    }
}
