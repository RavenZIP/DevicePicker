package com.ravenzip.devicepicker.features.main.user.company.screens.devices.add

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
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.common.model.UiEvent
import com.ravenzip.devicepicker.common.utils.base.UiEventEffect
import com.ravenzip.workshop.components.TopAppBar
import com.ravenzip.workshop.data.appbar.BackArrow
import com.ravenzip.workshop.data.icon.IconConfig
import com.ravenzip.workshop.data.icon.IconData

@Composable
fun AddDevicesToCompanyScreenScaffold(
    viewModel: AddDevicesToCompanyViewModel = hiltViewModel(),
    padding: PaddingValues,
    navigateTo: (route: String) -> Unit,
    navigateBack: () -> Unit,
) {
    val backArrow = remember {
        BackArrow(
            icon = IconData.ResourceIcon(R.drawable.i_back),
            iconConfig = IconConfig.Default,
            onClick = navigateBack,
        )
    }

    Scaffold(
        modifier = Modifier.padding(padding),
        topBar = { TopAppBar("Добавление устройства", backArrow = backArrow) },
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentAlignment = Alignment.TopCenter,
        ) {
            AddDevicesToCompanyScreenContent(viewModel = viewModel)
        }
    }

    UiEventEffect(viewModel.uiEvent) { event ->
        when (event) {
            is UiEvent.Navigate.ByRoute -> {
                navigateTo(event.route)
            }

            is UiEvent.Navigate.Back -> {
                navigateBack()
            }

            else -> {
                // do nothing
            }
        }
    }
}
