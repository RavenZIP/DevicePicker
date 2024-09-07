package com.ravenzip.devicepicker.ui.screens.main.device.info

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.viewmodels.home.DeviceInfoViewModel
import com.ravenzip.workshop.components.TopAppBar
import com.ravenzip.workshop.data.appbar.AppBarItem
import com.ravenzip.workshop.data.appbar.BackArrow
import com.ravenzip.workshop.data.icon.Icon
import com.ravenzip.workshop.data.icon.IconConfig

@Composable
fun DeviceInfoScaffold(
    deviceInfoViewModel: DeviceInfoViewModel = hiltViewModel<DeviceInfoViewModel>(),
    navigateBack: () -> Unit,
    padding: PaddingValues,
) {
    val topAppBarItems = rememberSaveable { generateDeviceInfoTopAppBarItems() }

    Scaffold(
        modifier = Modifier.padding(padding),
        topBar = {
            TopAppBar(
                title = "",
                backArrow =
                    BackArrow(
                        icon = Icon.ResourceIcon(R.drawable.i_back),
                        iconConfig = IconConfig.Default,
                        onClick = navigateBack,
                    ),
                items = topAppBarItems,
            )
        },
    ) { innerPadding ->
        DeviceInfoContent(deviceInfoViewModel = deviceInfoViewModel, padding = innerPadding)
    }

    DisposableEffect(Unit) { onDispose { deviceInfoViewModel.clearDeviceData() } }
}

private fun generateDeviceInfoTopAppBarItems(): List<AppBarItem> {
    val favouriteButton =
        AppBarItem(
            icon = Icon.ResourceIcon(R.drawable.i_heart),
            iconConfig = IconConfig.Small,
            onClick = {},
        )

    val compareButton =
        AppBarItem(
            icon = Icon.ResourceIcon(R.drawable.i_compare),
            iconConfig = IconConfig.Small,
            onClick = {},
        )

    return listOf(favouriteButton, compareButton)
}
