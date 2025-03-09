package com.ravenzip.devicepicker.features.main.user.company.screens.devices

import androidx.lifecycle.ViewModel
import com.ravenzip.devicepicker.common.model.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

@HiltViewModel
class CompanyDevicesViewModel @Inject constructor() : ViewModel() {
    val navigateTo = MutableSharedFlow<String>()
    val navigateBack = MutableSharedFlow<Unit>()

    val uiEvent =
        merge(
            navigateTo.map { route -> UiEvent.Navigate.ByRoute(route) },
            navigateBack.map { UiEvent.Navigate.Back },
        )
}
