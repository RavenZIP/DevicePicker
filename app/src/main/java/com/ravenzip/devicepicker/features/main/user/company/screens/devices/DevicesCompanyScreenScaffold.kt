package com.ravenzip.devicepicker.features.main.user.company.screens.devices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.common.model.UiEvent
import com.ravenzip.devicepicker.common.utils.base.UiEventEffect
import com.ravenzip.devicepicker.features.main.user.company.screens.info.CompanyInfoViewModel
import com.ravenzip.devicepicker.navigation.models.CompanyGraph
import com.ravenzip.workshop.components.TopAppBar
import com.ravenzip.workshop.data.appbar.BackArrow
import com.ravenzip.workshop.data.icon.IconConfig
import com.ravenzip.workshop.data.icon.IconData
import kotlinx.coroutines.launch

@Composable
fun DevicesCompanyScreenScaffold(
    companyInfoViewModel: CompanyInfoViewModel,
    viewModel: CompanyDevicesViewModel = hiltViewModel(),
    padding: PaddingValues,
    navigateTo: (route: String) -> Unit,
    navigateBack: () -> Unit,
) {
    val composableScope = rememberCoroutineScope()
    val backArrow = remember {
        BackArrow(
            icon = IconData.ResourceIcon(R.drawable.i_back),
            iconConfig = IconConfig.Default,
            onClick = { composableScope.launch { viewModel.navigateBack.emit(Unit) } },
        )
    }

    Scaffold(
        modifier = Modifier.padding(padding),
        topBar = { TopAppBar("Устройства", backArrow = backArrow) },
        floatingActionButton = {
            val devicesIsEmpty =
                companyInfoViewModel.devicesIsEmpty.collectAsStateWithLifecycle().value

            if (!devicesIsEmpty) {
                FloatingActionButton(
                    onClick = {
                        composableScope.launch {
                            viewModel.navigateTo.emit(CompanyGraph.ADD_DEVICES)
                        }
                    }
                ) {
                    Icon(
                        ImageVector.vectorResource(R.drawable.i_plus_octagon),
                        contentDescription = "",
                    )
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            DevicesCompanyScreenContent(companyInfoViewModel, viewModel)
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
