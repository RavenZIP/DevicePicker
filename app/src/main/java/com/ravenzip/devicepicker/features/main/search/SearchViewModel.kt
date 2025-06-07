package com.ravenzip.devicepicker.features.main.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.common.dummy.XIAOMI_12
import com.ravenzip.devicepicker.common.dummy.XIAOMI_14
import com.ravenzip.devicepicker.common.dummy.XIAOMI_15
import com.ravenzip.devicepicker.common.dummy.XIAOMI_PAD_7
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

    val searchResults =
        listOf(
            XIAOMI_12.convertToDeviceCompactExtended(),
            XIAOMI_14.convertToDeviceCompactExtended(),
            XIAOMI_PAD_7.convertToDeviceCompactExtended(),
            XIAOMI_15.convertToDeviceCompactExtended(),
        )
}
