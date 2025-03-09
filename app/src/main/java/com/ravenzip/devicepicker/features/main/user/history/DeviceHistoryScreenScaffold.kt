package com.ravenzip.devicepicker.features.main.user.history

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ravenzip.workshop.components.TopAppBar

@Composable
fun DeviceHistoryScreenScaffold(
    viewModel: DeviceHistoryViewModel = hiltViewModel(),
    padding: PaddingValues,
    navigateToDevice: (uid: String) -> Unit,
) {
    Scaffold(modifier = Modifier.padding(padding), topBar = { TopAppBar("История просмотров") }) {
        innerPadding ->
        DeviceHistoryScreenContent(
            viewModel = viewModel,
            padding = innerPadding,
            navigateToDevice = navigateToDevice,
        )
    }
}
