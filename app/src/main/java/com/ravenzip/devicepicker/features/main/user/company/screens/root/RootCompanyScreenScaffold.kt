package com.ravenzip.devicepicker.features.main.user.company.screens.root

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.common.components.BottomContainer2
import com.ravenzip.devicepicker.common.model.UiEvent
import com.ravenzip.devicepicker.navigation.NavigationParams
import com.ravenzip.devicepicker.common.utils.base.UiEventEffect
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.components.TopAppBar
import com.ravenzip.workshop.data.appbar.BackArrow
import com.ravenzip.workshop.data.icon.IconConfig
import com.ravenzip.workshop.data.icon.IconData
import kotlinx.coroutines.launch

@Composable
fun CompanyRootScreenScaffold(
    viewModel: CompanyRootViewModel = hiltViewModel(),
    padding: PaddingValues,
    navigationParams: NavigationParams,
) {
    val composableScope = rememberCoroutineScope()

    val backArrow = remember {
        BackArrow(
            icon = IconData.ResourceIcon(R.drawable.i_back),
            iconConfig = IconConfig.Default,
            onClick = { composableScope.launch { viewModel.navigateBackToParent.emit(Unit) } },
        )
    }

    Scaffold(
        modifier = Modifier.padding(padding),
        topBar = { TopAppBar("Компания", backArrow = backArrow) },
        bottomBar = {
            BottomContainer2(padding = PaddingValues(top = 20.dp, bottom = 10.dp)) {
                SimpleButton(text = "Продолжить") {
                    composableScope.launch { viewModel.navigateByCompanyScreenType.emit(Unit) }
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CompanyRootScreenContent(viewModel)
        }
    }

    UiEventEffect(viewModel.uiEvent) { event ->
        when (event) {
            is UiEvent.Navigate.ByRoute -> navigationParams.navigateTo(event.route)

            is UiEvent.Navigate.WithoutBackStack ->
                navigationParams.navigateToWithClearBackStack(event.route)

            is UiEvent.Navigate.Parent -> navigationParams.navigateBackToParent()

            else -> {
                // do nothing
            }
        }
    }
}
