package com.ravenzip.devicepicker.viewmodels.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.model.device.compact.DeviceCompact.Companion.convertToDeviceCompactExtended
import com.ravenzip.devicepicker.repositories.SharedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class DeviceHistoryViewModel @Inject constructor(private val sharedRepository: SharedRepository) :
    ViewModel() {
    val deviceHistory =
        sharedRepository.allDevices
            .combine(sharedRepository.deviceHistory) { allDevices, deviceHistory ->
                allDevices.filter { it.uid in deviceHistory }
            }
            .map { devices -> devices.map { device -> device.convertToDeviceCompactExtended() } }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = listOf(),
            )

    fun tryToUpdateFavourites(deviceUid: String) {
        viewModelScope.launch { sharedRepository.tryToUpdateFavourites(deviceUid) }
    }

    fun tryToUpdateCompares(deviceUid: String) {
        // TODO
        /// viewModelScope.launch { sharedRepository.tryToUpdateFavourites(deviceUid) }
    }
}
