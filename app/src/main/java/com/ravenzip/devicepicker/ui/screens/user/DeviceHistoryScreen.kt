package com.ravenzip.devicepicker.ui.screens.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
        modifier = Modifier.fillMaxWidth().padding(padding).padding(top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        items(deviceHistoryState) { device ->
            RowDeviceCard(device = device) {
                deviceHistoryViewModel.setDeviceQueryParams(device.uid, device.brand, device.model)
                navigateToDevice()
            }
        }
    }
}
