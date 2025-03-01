package com.ravenzip.devicepicker.ui.screens.main.user.company.root

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.ui.screens.main.user.company.viewmodel.CompanyRootViewModel
import com.ravenzip.devicepicker.viewmodels.base.UiEventEffect
import com.ravenzip.workshop.components.TopAppBar
import com.ravenzip.workshop.data.appbar.BackArrow
import com.ravenzip.workshop.data.icon.IconConfig
import com.ravenzip.workshop.data.icon.IconData

@Composable
fun CompanyRootScreenScaffold(
    viewModel: CompanyRootViewModel = hiltViewModel(),
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
        topBar = { TopAppBar("Компания", backArrow = backArrow) },
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CompanyRootScreenContent(viewModel)
        }
    }

    UiEventEffect(viewModel.uiEvent) { event -> navigateTo(event.route) }
}
