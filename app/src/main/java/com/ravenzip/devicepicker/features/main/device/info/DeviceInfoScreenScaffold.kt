package com.ravenzip.devicepicker.features.main.device.info

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.common.model.UiState
import com.ravenzip.devicepicker.common.utils.extension.showMessage
import com.ravenzip.devicepicker.common.ErrorScreenCard
import com.ravenzip.devicepicker.common.utils.base.UiEventEffect
import com.ravenzip.workshop.components.SnackBar
import com.ravenzip.workshop.components.Spinner
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
    val uiState = viewModel.device.collectAsStateWithLifecycle().value
    val topAppBarItems = viewModel.topAppBarButtons.collectAsStateWithLifecycle().value

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
        when (uiState) {
            is UiState.Loading -> {
                Spinner(text = "Загрузка...")
            }

            is UiState.Success -> {
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                    DeviceInfoScreenContent(viewModel = viewModel, device = uiState.data)
                }
            }

            is UiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    ErrorScreenCard(
                        text = "Произошла ошибка",
                        description = "${uiState.message}. Пожалуйста, попробуйте позже",
                    )
                }
            }
        }
    }

    UiEventEffect(viewModel.uiEvent) { event ->
        viewModel.snackBarHostState.showMessage(event.message)
    }

    SnackBar(snackBarHostState = viewModel.snackBarHostState)
}
