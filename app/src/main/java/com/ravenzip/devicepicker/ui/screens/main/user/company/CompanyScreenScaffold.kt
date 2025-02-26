package com.ravenzip.devicepicker.ui.screens.main.user.company

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.extensions.functions.showError
import com.ravenzip.devicepicker.extensions.functions.showSuccess
import com.ravenzip.devicepicker.state.UiEvent
import com.ravenzip.devicepicker.viewmodels.base.UiEventEffect
import com.ravenzip.devicepicker.viewmodels.user.CompanyViewModel
import com.ravenzip.workshop.components.SnackBar
import com.ravenzip.workshop.components.Spinner
import com.ravenzip.workshop.components.TopAppBar
import com.ravenzip.workshop.data.appbar.BackArrow
import com.ravenzip.workshop.data.icon.IconConfig
import com.ravenzip.workshop.data.icon.IconData

@Composable
fun CompanyScreenScaffold(
    viewModel: CompanyViewModel = hiltViewModel(),
    padding: PaddingValues,
    navigateBack: () -> Unit,
) {
    val spinner = viewModel.spinner.collectAsStateWithLifecycle().value

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
        CompanyScreenContent(viewModel, innerPadding)

        UiEventEffect(viewModel.uiEvent) { event ->
            when (event) {
                is UiEvent.ShowSnackBar.Success -> {
                    viewModel.snackBarHostState.showSuccess(event.message)
                }

                is UiEvent.ShowSnackBar.Error -> {
                    viewModel.snackBarHostState.showError(event.message)
                }

                else -> {
                    // do nothing
                }
            }
        }
    }

    SnackBar(viewModel.snackBarHostState)

    if (spinner.isLoading) {
        Spinner(spinner.text)
    }
}
