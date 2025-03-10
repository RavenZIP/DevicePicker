package com.ravenzip.devicepicker.features.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.common.enums.TagsEnum
import com.ravenzip.devicepicker.common.repositories.SharedRepository
import com.ravenzip.workshop.forms.state.FormState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(private val sharedRepository: SharedRepository) :
    ViewModel() {
    /** Все устройства (компактная модель) */
    private val _allDevices = sharedRepository.allDevices

    val selectedCategory = FormState(initialValue = TagsEnum.POPULAR)

    val devicesInSelectedCategory =
        selectedCategory.valueChanges
            .combine(_allDevices) { category, devices ->
                devices.filter { device -> device.tags.contains(category) }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = listOf(),
            )

    init {
        viewModelScope.launch { sharedRepository.getDeviceCompactList() }
    }
}
