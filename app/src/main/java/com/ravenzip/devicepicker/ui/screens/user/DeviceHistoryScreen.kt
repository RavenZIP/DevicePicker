package com.ravenzip.devicepicker.ui.screens.user

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ravenzip.devicepicker.model.User
import com.ravenzip.devicepicker.model.device.compact.DeviceCompact
import com.ravenzip.devicepicker.ui.components.RowDeviceCard
import kotlinx.coroutines.flow.StateFlow

@Composable
fun DeviceHistoryScreen(
    padding: PaddingValues,
    userDataByViewModel: StateFlow<User>,
    createDeviceHistoryList: (List<String>) -> List<DeviceCompact>,
    getDevice: (uid: String, brand: String, model: String) -> Unit,
    navigateToDevice: () -> Unit,
) {
    // TODO переделать (временная заглушка)
    val userState = userDataByViewModel.collectAsState().value
    val deviceHistoryList = remember { createDeviceHistoryList(userState.deviceHistory) }

    LazyColumn(
        modifier = Modifier.fillMaxWidth().padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(deviceHistoryList) { device ->
            RowDeviceCard(device = device) {
                getDevice(device.uid, device.brand, device.model)
                navigateToDevice()
            }
        }
    }
}
