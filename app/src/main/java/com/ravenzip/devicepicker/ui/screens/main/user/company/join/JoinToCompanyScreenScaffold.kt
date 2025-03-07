package com.ravenzip.devicepicker.ui.screens.main.user.company.join

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.extensions.functions.inverseColors
import com.ravenzip.devicepicker.extensions.functions.showError
import com.ravenzip.devicepicker.navigation.NavigationParams
import com.ravenzip.devicepicker.state.UiEvent
import com.ravenzip.devicepicker.ui.components.BottomContainer2
import com.ravenzip.devicepicker.ui.screens.main.user.company.viewmodel.JoinToCompanyViewModel
import com.ravenzip.devicepicker.viewmodels.base.UiEventEffect
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.components.Spinner
import com.ravenzip.workshop.components.TopAppBar
import com.ravenzip.workshop.data.appbar.BackArrow
import com.ravenzip.workshop.data.icon.IconConfig
import com.ravenzip.workshop.data.icon.IconData
import kotlinx.coroutines.launch

@Composable
fun CompanyScreenJoinScaffold(
    viewModel: JoinToCompanyViewModel = hiltViewModel(),
    padding: PaddingValues,
    navigationParams: NavigationParams,
) {
    val spinnerState = viewModel.spinner.collectAsStateWithLifecycle().value
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
                SimpleButton(text = "Назад", colors = ButtonDefaults.inverseColors()) {
                    composableScope.launch { viewModel.navigateBack.emit(Unit) }
                }

                Spacer(modifier = Modifier.height(15.dp))

                SimpleButton(text = "Присоединиться") {
                    composableScope.launch { viewModel.joinToCompany.emit(Unit) }
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CompanyScreenJoinContent(viewModel)
        }
    }

    UiEventEffect(viewModel.uiEvent) { event ->
        when (event) {
            is UiEvent.Navigate.ByRoute ->
                navigationParams.navigateToWithClearBackStack(event.route)

            is UiEvent.Navigate.Back -> navigationParams.navigateBack()

            is UiEvent.Navigate.Parent -> navigationParams.navigateBackToParent()

            is UiEvent.ShowSnackBar.Error -> {
                viewModel.snackBarHostState.showError(event.message)
            }

            else -> {
                // do nothing
            }
        }
    }

    if (spinnerState.isLoading) {
        Spinner(spinnerState.text)
    }
}
