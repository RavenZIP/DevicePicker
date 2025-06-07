package com.ravenzip.devicepicker.features.main.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.common.enums.TagsEnum
import com.ravenzip.devicepicker.common.model.device.compact.DeviceCompact
import com.ravenzip.devicepicker.common.model.device.compact.DeviceCompact.Companion.convertToDeviceCompactExtended
import com.ravenzip.devicepicker.common.repositories.BrandRepository
import com.ravenzip.devicepicker.common.repositories.DeviceTypeRepository
import com.ravenzip.workshop.forms.control.FormControl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
@Inject
constructor(
    private val brandRepository: BrandRepository,
    private val deviceTypeRepository: DeviceTypeRepository,
) : ViewModel() {
    private val _brandList = MutableStateFlow(listOf<String>())
    private val _deviceTypeList = MutableStateFlow(listOf<String>())

    val searchControl = FormControl("")

    val wasSearched = mutableStateOf(false)

    val brandList = _brandList.asStateFlow()
    val deviceTypeList = _deviceTypeList.asStateFlow()

    init {
        brandRepository
            .getBrandList()
            .zip(deviceTypeRepository.getDeviceTypeList()) { brandList, deviceTypeList ->
                _brandList.update { brandList }
                _deviceTypeList.update { deviceTypeList }
            }
            .launchIn(viewModelScope)
    }

    val first =
        DeviceCompact(
            uid = "-ORCgcpsKB17XhBXQQy2",
            type = "Смартфон",
            model = "Xiaomi 12",
            diagonal = 0.0,
            cpu = "",
            battery = 0,
            camera = 0,
            rating = 4.8,
            reviewsCount = 510,
            brand = "Xiaomi",
            tags = listOf(TagsEnum.USER_SELECTION, TagsEnum.SECURITY),
            imageUrl =
                "https://firebasestorage.googleapis.com/v0/b/devicepicker-d290d.appspot.com/o/Xiaomi%2FXiaomi%2012%2FXiaomi%2012.webp?alt=media&token=c00b6c9e-6b0a-421e-9c42-11cfca5228ab",
        )

    val second =
        DeviceCompact(
            uid = "-O-Wwzuu0xzWMtt5x4SZ",
            type = "Смартфон",
            model = "Xiaomi 14",
            diagonal = 0.0,
            cpu = "",
            battery = 0,
            camera = 0,
            rating = 4.83,
            reviewsCount = 132,
            brand = "Xiaomi",
            tags = listOf(TagsEnum.HIGH_PERFORMANCE, TagsEnum.LIGHT_WEIGHT, TagsEnum.SECURITY),
            imageUrl =
                "https://firebasestorage.googleapis.com/v0/b/devicepicker-d290d.appspot.com/o/Xiaomi%2FXiaomi%2014%2FXiaomi%2014.webp?alt=media&token=0083b1cf-a67c-4d91-b9d9-54d16bcd21da",
        )

    val third =
        DeviceCompact(
            uid = "-OR7_1A7Lc1GWvwWhmMK",
            type = "Планшет",
            model = "Xiaomi Pad 7",
            diagonal = 0.0,
            cpu = "",
            battery = 0,
            camera = 0,
            rating = 4.76,
            reviewsCount = 86,
            brand = "Xiaomi",
            tags = listOf(TagsEnum.NEW_MODEL, TagsEnum.HIGH_PERFORMANCE),
            imageUrl =
                "https://firebasestorage.googleapis.com/v0/b/devicepicker-d290d.appspot.com/o/Xiaomi%2FXiaomi%20Pad%207%2FXiaomi%20Pad%207.webp?alt=media&token=8f84839c-0999-47f4-9d3b-88c4931321f2",
        )

    val fourth =
        DeviceCompact(
            uid = "-OQod5-AcSR7wl-ucTmV",
            type = "Смартфон",
            model = "Xiaomi 15",
            diagonal = 0.0,
            cpu = "",
            battery = 0,
            camera = 0,
            rating = 4.83,
            reviewsCount = 132,
            brand = "Xiaomi",
            tags = listOf(TagsEnum.NEW_MODEL, TagsEnum.HIGH_PERFORMANCE, TagsEnum.LIGHT_WEIGHT),
            imageUrl =
                "https://firebasestorage.googleapis.com/v0/b/devicepicker-d290d.appspot.com/o/Xiaomi%2FXiaomi%2015%2FXiaomi%2015.webp?alt=media&token=9f765de0-67ef-408a-8beb-5234e194aeef",
        )

    val searchResults =
        listOf(
            first.convertToDeviceCompactExtended(),
            second.convertToDeviceCompactExtended(),
            third.convertToDeviceCompactExtended(),
            fourth.convertToDeviceCompactExtended(),
        )
}
