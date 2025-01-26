package com.ravenzip.devicepicker.ui.screens.main.user.history

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ravenzip.devicepicker.viewmodels.user.DeviceHistoryViewModel
import com.ravenzip.workshop.components.TopAppBar

@Composable
fun DeviceHistoryScreenScaffold(
    deviceHistoryViewModel: DeviceHistoryViewModel = hiltViewModel<DeviceHistoryViewModel>(),
    padding: PaddingValues,
    navigateToDevice: (uid: String) -> Unit,
) {
    Scaffold(modifier = Modifier.padding(padding), topBar = { TopAppBar("История просмотров") }) {
        innerPadding ->
        DeviceHistoryScreenContent(
            deviceHistoryViewModel = deviceHistoryViewModel,
            padding = innerPadding,
            navigateToDevice = navigateToDevice,
        )
    }
}
