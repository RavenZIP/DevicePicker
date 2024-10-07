package com.ravenzip.devicepicker.viewmodels.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.repositories.BrandRepository
import com.ravenzip.devicepicker.repositories.DeviceTypeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel
@Inject
constructor(
    private val brandRepository: BrandRepository,
    private val deviceTypeRepository: DeviceTypeRepository,
) : ViewModel() {
    private val _brandList = MutableStateFlow(listOf<String>())
    private val _deviceTypeList = MutableStateFlow(listOf<String>())

    val brandList = _brandList.asStateFlow()
    val deviceTypeList = _deviceTypeList.asStateFlow()

    init {
        viewModelScope.launch {
            brandRepository
                .getBrandList()
                .zip(deviceTypeRepository.getDeviceTypeList()) { brandList, deviceTypeList ->
                    _brandList.update { brandList }
                    _deviceTypeList.update { deviceTypeList }
                }
                .collect {}
        }
    }
}
