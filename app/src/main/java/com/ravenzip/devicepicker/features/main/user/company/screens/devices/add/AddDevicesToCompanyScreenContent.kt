package com.ravenzip.devicepicker.features.main.user.company.screens.devices.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravenzip.devicepicker.common.components.ColumnDeviceCard
import kotlinx.coroutines.launch

@Composable
fun AddDevicesToCompanyScreenContent(viewModel: AddDevicesToCompanyViewModel) {
    val devices = viewModel.devices.collectAsStateWithLifecycle().value
    val composableScope = rememberCoroutineScope()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth(0.9f).fillMaxHeight(),
        contentPadding = PaddingValues(vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(devices) { device ->
            ColumnDeviceCard(
                device = device,
                onClick = { composableScope.launch { viewModel.navigateTo.emit(device.uid) } },
            )
        }
    }
}
