package com.ravenzip.devicepicker.viewmodels.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.repositories.SharedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class FavouritesScreenViewModel
@Inject
constructor(private val sharedRepository: SharedRepository) : ViewModel() {
    val favourites =
        sharedRepository.allDevices
            .combine(sharedRepository.favourites) { allDevices, favourites ->
                allDevices.filter { it.uid in favourites }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = listOf(),
            )

    fun setDeviceQueryParams(uid: String, brand: String, model: String) {
        sharedRepository.setDeviceQueryParams(uid, brand, model)
    }
}
