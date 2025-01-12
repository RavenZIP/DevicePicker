package com.ravenzip.devicepicker.ui.screens.main.device.info

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.viewmodels.home.DeviceInfoViewModel
import com.ravenzip.workshop.components.SnackBar
import com.ravenzip.workshop.components.TopAppBar
import com.ravenzip.workshop.data.appbar.BackArrow
import com.ravenzip.workshop.data.icon.Icon
import com.ravenzip.workshop.data.icon.IconConfig

@Composable
fun DeviceInfoScaffold(
    deviceInfoViewModel: DeviceInfoViewModel = hiltViewModel<DeviceInfoViewModel>(),
    navigateBack: () -> Unit,
    padding: PaddingValues,
) {
    val topAppBarItems = deviceInfoViewModel.topAppBarButtons.collectAsState().value
    val snackBarHostState = remember { deviceInfoViewModel.snackBarHostState }

    val backArrow = remember {
        BackArrow(
            icon = Icon.ResourceIcon(R.drawable.i_back),
            iconConfig = IconConfig.Default,
            onClick = navigateBack,
        )
    }

    Scaffold(
        modifier = Modifier.padding(padding),
        topBar = { TopAppBar(title = "", backArrow = backArrow, items = topAppBarItems) },
    ) { innerPadding ->
        DeviceInfoContent(deviceInfoViewModel = deviceInfoViewModel, padding = innerPadding)
    }

    SnackBar(snackBarHostState = snackBarHostState)
}
