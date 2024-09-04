package com.ravenzip.devicepicker.ui.screens.user

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ravenzip.devicepicker.ui.components.RowDeviceCard
import com.ravenzip.devicepicker.viewmodels.user.DeviceHistoryViewModel

@Composable
fun DeviceHistoryScreen(
    deviceHistoryViewModel: DeviceHistoryViewModel = hiltViewModel<DeviceHistoryViewModel>(),
    padding: PaddingValues,
    navigateToDevice: () -> Unit,
) {
    val deviceHistoryState = deviceHistoryViewModel.deviceHistory.collectAsState().value

    LazyColumn(
        modifier = Modifier.fillMaxWidth().padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(deviceHistoryState) { device ->
            RowDeviceCard(device = device) {
                //// getDevice(device.uid, device.brand, device.model)
                navigateToDevice()
            }
        }
    }
}
