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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
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

    private val _isCompare =
        _deviceStateIsSuccess
            .combine(sharedRepository.compares) { device, compares -> device.data.uid in compares }
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val isFavourite = _isFavourite.filter { isFavourite -> isFavourite }
    private val isNotFavourite = _isFavourite.filter { isFavourite -> !isFavourite }

    private val isCompare = _isCompare.filter { isCompare -> isCompare }
    private val isNotCompare = _isCompare.filter { isCompare -> !isCompare }

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
                .take(1)
                .flatMapLatest { params ->
                    val cachedDevice = sharedRepository.getCachedDevice(params!!.uid)

                    return@flatMapLatest if (cachedDevice == null) {
                        getDeviceByBrandAndUid(params.uid, params.brand, params.model)
                    } else {
                        flow { UiState.Success(cachedDevice) }
                    }
                }
                .collect { deviceState -> _device.update { deviceState } }
        }

        viewModelScope.launch {
            // drop(2) чтоб не отображать снэкбар при заходе на экран
            merge(
                    isFavourite.map { "Устройство добавлено в избранное" },
                    isNotFavourite.map { "Устройство удалено из избранного" },
                    isCompare.map { "Устройство добавлено в список сравнения" },
                    isNotCompare.map { "Устройство удалено из списка сравнения" },
                )
                .drop(2)
                .collect { message -> snackBarHostState.showMessage(message) }
        }
    }

    /** Получение устройства по бренду и уникальному идентификатору */
    private fun getDeviceByBrandAndUid(uid: String, brand: String, model: String) =
        deviceRepository.getDeviceByBrandAndUid(brand, uid).zip(
            imageRepository.getImageUrls(brand, model)
        ) { device, imageUrls ->
            if (device != null) {
                val deviceWithImageUrls = device.copy(imageUrls = imageUrls)

                sharedRepository.updateCachedDevices(deviceWithImageUrls)
                return@zip UiState.Success(deviceWithImageUrls)
            } else {
                val errorMessage = "При выполении запроса произошла ошибка"
                return@zip UiState.Error(errorMessage)
            }
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
