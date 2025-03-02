package com.ravenzip.devicepicker.ui.screens.main.user.company.create

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.extensions.functions.showError
import com.ravenzip.devicepicker.navigation.NavigationParams
import com.ravenzip.devicepicker.state.UiEvent
import com.ravenzip.devicepicker.ui.screens.main.user.company.viewmodel.CreateCompanyViewModel
import com.ravenzip.devicepicker.viewmodels.base.UiEventEffect
import com.ravenzip.workshop.components.Spinner
import com.ravenzip.workshop.components.TopAppBar
import com.ravenzip.workshop.data.appbar.BackArrow
import com.ravenzip.workshop.data.icon.IconConfig
import com.ravenzip.workshop.data.icon.IconData
import kotlinx.coroutines.launch

@Composable
fun CompanyScreenCreateScaffold(
    viewModel: CreateCompanyViewModel = hiltViewModel(),
    padding: PaddingValues,
    navigationParams: NavigationParams,
) {
    val composableScope = rememberCoroutineScope()
    val spinnerState = viewModel.spinner.collectAsStateWithLifecycle().value

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
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CompanyScreenCreateContent(viewModel)
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
