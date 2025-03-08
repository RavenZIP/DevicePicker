package com.ravenzip.devicepicker.ui.screens.main.user.company.devices

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.ui.screens.main.user.company.viewmodel.CompanyInfoViewModel
import com.ravenzip.workshop.components.TopAppBar
import com.ravenzip.workshop.data.appbar.BackArrow
import com.ravenzip.workshop.data.icon.IconConfig
import com.ravenzip.workshop.data.icon.IconData

@Composable
fun DevicesCompanyScreenScaffold(
    viewModel: CompanyInfoViewModel,
    padding: PaddingValues,
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
        topBar = { TopAppBar("Устройства", backArrow = backArrow) },
        floatingActionButton = {
            val devicesIsEmpty = viewModel.devicesIsEmpty.collectAsStateWithLifecycle().value

            if (!devicesIsEmpty) {
                FloatingActionButton(onClick = {}) {
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
            DevicesCompanyScreenContent(viewModel)
        }
    }
}
