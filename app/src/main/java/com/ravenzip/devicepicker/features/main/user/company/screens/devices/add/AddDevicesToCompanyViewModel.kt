package com.ravenzip.devicepicker.features.main.user.company.screens.devices.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.common.model.AlertDialog
import com.ravenzip.devicepicker.common.model.UiEvent
import com.ravenzip.devicepicker.common.model.device.compact.DeviceCompact.Companion.convertToDeviceCompactExtended
import com.ravenzip.devicepicker.common.repositories.SharedRepository
import com.ravenzip.workshop.forms.control.FormControl
import com.ravenzip.workshop.forms.group.FormGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class AddDevicesToCompanyViewModel @Inject constructor(sharedRepository: SharedRepository) :
    ViewModel() {
    val navigateTo = MutableSharedFlow<String>()
    val navigateBack = MutableSharedFlow<Unit>()

    val form =
        FormGroup(
            AddDevicesToCompanyForm(
                deviceCounter = FormControl(initialValue = ""),
                search = FormControl(initialValue = ""),
            )
        )

    val alertDialog = AlertDialog()

    val devices =
        sharedRepository.allDevices
            .map { devices -> devices.map { device -> device.convertToDeviceCompactExtended() } }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = listOf(),
            )

    val uiEvent =
        merge(
            navigateTo.map { route -> UiEvent.Navigate.ByRoute(route) },
            navigateBack.map { UiEvent.Navigate.Back },
        )

    init {
        alertDialog.isShowed.onEach { form.controls.deviceCounter.reset() }.launchIn(viewModelScope)
    }
}
