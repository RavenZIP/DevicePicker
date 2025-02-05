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
import com.ravenzip.workshop.data.icon.IconConfig
import com.ravenzip.workshop.data.icon.IconData

@Composable
fun DeviceInfoScreenScaffold(
    viewModel: DeviceInfoViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    padding: PaddingValues,
) {
    val topAppBarItems = viewModel.topAppBarButtons.collectAsState().value

    val backArrow = remember {
        BackArrow(
            icon = IconData.ResourceIcon(R.drawable.i_back),
            iconConfig = IconConfig.Default,
            onClick = navigateBack,
        )
    }

    Scaffold(
        modifier = Modifier.padding(padding),
        topBar = { TopAppBar(title = "", backArrow = backArrow, items = topAppBarItems) },
    ) { innerPadding ->
        DeviceInfoScreenContent(viewModel = viewModel, padding = innerPadding)
    }

    SnackBar(snackBarHostState = viewModel.snackBarHostState)
}
