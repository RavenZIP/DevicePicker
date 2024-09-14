package com.ravenzip.devicepicker.viewmodels.home

import androidx.compose.material3.SnackbarHostState
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

@HiltViewModel
class DeviceInfoViewModel
@Inject
constructor(
    private val deviceRepository: DeviceRepository,
    private val imageRepository: ImageRepository,
    private val sharedRepository: SharedRepository,
) : ViewModel() {
    /** Текущее выбранное устройство */
    private val _device = MutableStateFlow<UiState<Device>>(UiState.Loading("Загрузка..."))

    /** Устройство успешно получено */
    private val _deviceStateIsSuccess =
        _device
            .filter { deviceState -> deviceState is UiState.Success }
            .map { deviceState -> deviceState as UiState.Success<Device> }
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    val device = _device.asStateFlow()
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
            .combine(sharedRepository.favourites) { device, favourites ->
                device.data.uid in favourites
            }
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

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
            sharedRepository.deviceQueryParams
                .filter { params -> params != null }
                .collect { params ->
                    val cachedDevice = sharedRepository.getCachedDevice(params!!.uid)

                    if (cachedDevice == null) {
                        getDeviceByBrandAndUid(params.uid, params.brand, params.model)
                    } else {
                        _device.update { UiState.Success(cachedDevice) }
                    }

                    if (_device.value is UiState.Success) {
                        sharedRepository.tryToUpdateDeviceHistory(params.uid)
                    }
                }
        }

        viewModelScope.launch {
            // drop(1) чтоб не отображать снэкбар при заходе на экран
            _isFavourite.drop(1).collect { isFavourite ->
                if (isFavourite) {
                    snackBarHostState.showMessage("Устройство добавлено в избранное")
                } else {
                    snackBarHostState.showMessage("Устройство удалено из избранного")
                }
            }
        }
    }

    /** Получение устройства по бренду и уникальному идентификатору */
    private suspend fun getDeviceByBrandAndUid(uid: String, brand: String, model: String) {
        deviceRepository
            .getDeviceByBrandAndUid(brand, uid)
            .zip(imageRepository.getImageUrls(brand, model)) { device, imageUrls ->
                if (device != null) {
                    val deviceWithImageUrls = device.copy(imageUrls = imageUrls)

                    _device.update { UiState.Success(deviceWithImageUrls) }
                    sharedRepository.updateCachedDevices(deviceWithImageUrls)
                } else {
                    val errorMessage = "При выполении запроса произошла ошибка"
                    _device.update { UiState.Error(errorMessage) }
                }
            }
            .collect {}
    }

    fun clearDeviceData() {
        sharedRepository.clearDeviceQueryParams()
    }

    private fun tryToUpdateFavourites() {
        viewModelScope.launch {
            // Убеждаем компилятор в том, что UiState.Success,
            // т.к. сами кнопки появляются лишь тогда, когда состояние Success
            sharedRepository.tryToUpdateFavourites(
                (_device.value as UiState.Success<Device>).data.uid
            )
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
                onClick = { tryToUpdateFavourites() },
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
