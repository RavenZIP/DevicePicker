package com.ravenzip.devicepicker.screens.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ravenzip.devicepicker.viewmodels.DeviceViewModel

@Composable
fun DeviceInfoScreen(padding: PaddingValues, deviceViewModel: DeviceViewModel) {
    val device = deviceViewModel.device.collectAsState().value

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { Text(device.brand) }
    }
}
