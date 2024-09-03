package com.ravenzip.devicepicker.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.ravenzip.devicepicker.SharedRepository
import com.ravenzip.devicepicker.repositories.DeviceRepository
import com.ravenzip.devicepicker.repositories.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeviceInfoViewModel
@Inject
constructor(
    private val deviceRepository: DeviceRepository,
    private val imageRepository: ImageRepository,
    private val sharedRepository: SharedRepository,
) : ViewModel() {
    val device = sharedRepository.device

    init {
        Log.d("DeviceInfoViewModel", "init")
    }
}
