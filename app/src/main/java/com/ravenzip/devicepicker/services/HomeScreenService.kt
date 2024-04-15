package com.ravenzip.devicepicker.services

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import com.ravenzip.devicepicker.data.device.compact.DeviceCompact
import com.ravenzip.devicepicker.data.result.ImageResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class HomeScreenService @Inject constructor() : ViewModel() {
    private val _popularDevices = MutableStateFlow(mutableListOf<DeviceCompact>())
    private val _lowPriceDevices = MutableStateFlow(mutableListOf<DeviceCompact>())
    private val _highPerformanceDevices = MutableStateFlow(mutableListOf<DeviceCompact>())
    private val _theBestDevices = MutableStateFlow(mutableListOf<MutableList<DeviceCompact>>())
    private val _recentlyViewedDevices = MutableStateFlow(mutableListOf<DeviceCompact>())

    val popularDevices = _popularDevices.asStateFlow()
    val lowPriceDevices = _lowPriceDevices.asStateFlow()
    val highPerformanceDevices = _highPerformanceDevices.asStateFlow()
    val theBestDevices = _theBestDevices.asStateFlow()
    val recentlyViewedDevices = _recentlyViewedDevices.asStateFlow()

    fun setDevicesFromCategories(devices: MutableList<DeviceCompact>) {
        val popularDevices = devices.filter { it.tags.popular }
        val lowPriceDevices = devices.filter { it.tags.lowPrice }
        val highPerformance = devices.filter { it.tags.highPerformance }
        val theBest = devices.filter { it.tags.theBest }

        _popularDevices.value.addAll(popularDevices)
        _lowPriceDevices.value.addAll(lowPriceDevices)
        _highPerformanceDevices.value.addAll(highPerformance)
        theBest.forEach {
            val listIndex =
                _theBestDevices.value.indexOfFirst { deviceList -> deviceList[0].brand == it.brand }

            if (listIndex >= 0) _theBestDevices.value[listIndex].add(it)
            else _theBestDevices.value.add(mutableListOf(it))
        }

        // TODO фильтровать недавно просмотренные
        _recentlyViewedDevices.value.add(DeviceCompact())
    }

    fun tryToSetImageFromPopularDevices(image: ImageResult<ImageBitmap>) {
        val index = _popularDevices.value.indexOfFirst { it.model === image.imageName }
        if (index != -1) {
            _popularDevices.value[index] = _popularDevices.value[index].copy(image = image.value)
        }
    }

    fun tryToSetImageFromLowPriceDevices(image: ImageResult<ImageBitmap>) {
        val index = _lowPriceDevices.value.indexOfFirst { it.model === image.imageName }
        if (index != -1) {
            _lowPriceDevices.value[index] = _lowPriceDevices.value[index].copy(image = image.value)
        }
    }

    fun tryToSetImageFromHighPerformanceDevices(image: ImageResult<ImageBitmap>) {
        val index = _highPerformanceDevices.value.indexOfFirst { it.model === image.imageName }
        if (index != -1) {
            _highPerformanceDevices.value[index] =
                _highPerformanceDevices.value[index].copy(image = image.value)
        }
    }
}
