package com.ravenzip.devicepicker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.repositories.SharedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class DeviceHistoryViewModel @Inject constructor(sharedRepository: SharedRepository) : ViewModel() {
    val deviceHistory =
        sharedRepository.allDevices
            .combine(sharedRepository.userData) { allDevices, userData ->
                allDevices.filter { it.uid in userData.deviceHistory }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = listOf(),
            )
}
