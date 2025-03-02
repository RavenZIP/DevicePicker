package com.ravenzip.devicepicker.ui.screens.main.user.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravenzip.devicepicker.ui.components.RowDeviceCard
import com.ravenzip.devicepicker.viewmodels.user.DeviceHistoryViewModel

@Composable
fun DeviceHistoryScreenContent(
    viewModel: DeviceHistoryViewModel,
    padding: PaddingValues,
    navigateToDevice: (uid: String) -> Unit,
) {
    val deviceHistoryState = viewModel.deviceHistory.collectAsStateWithLifecycle().value

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        items(deviceHistoryState) { device ->
            RowDeviceCard(
                device = device,
                onFavouriteClick = { viewModel.tryToUpdateFavourites(device.uid) },
                onCompareClick = { viewModel.tryToUpdateCompares(device.uid) },
                onCardClick = { navigateToDevice(device.uid) },
            )
        }
    }
}
