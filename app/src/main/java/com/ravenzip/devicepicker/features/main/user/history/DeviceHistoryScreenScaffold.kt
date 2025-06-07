package com.ravenzip.devicepicker.features.main.user.history

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ravenzip.devicepicker.R
import com.ravenzip.workshop.components.TopAppBar
import com.ravenzip.workshop.data.appbar.BackArrow
import com.ravenzip.workshop.data.icon.IconConfig
import com.ravenzip.workshop.data.icon.IconData

@Composable
fun DeviceHistoryScreenScaffold(
    viewModel: DeviceHistoryViewModel = hiltViewModel(),
    padding: PaddingValues,
    navigateToUserProfile: () -> Unit,
    navigateToDevice: (uid: String) -> Unit,
) {
    val backArrow = remember {
        BackArrow(
            icon = IconData.ResourceIcon(R.drawable.i_back),
            iconConfig = IconConfig.Default,
            onClick = navigateToUserProfile,
        )
    }

    Scaffold(
        modifier = Modifier.padding(padding),
        topBar = { TopAppBar("История просмотров", backArrow = backArrow) },
    ) { innerPadding ->
        DeviceHistoryScreenContent(
            viewModel = viewModel,
            padding = innerPadding,
            navigateToDevice = navigateToDevice,
        )
    }
}
