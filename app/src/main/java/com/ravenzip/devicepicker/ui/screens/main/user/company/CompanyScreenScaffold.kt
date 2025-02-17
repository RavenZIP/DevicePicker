package com.ravenzip.devicepicker.ui.screens.main.user.company

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravenzip.devicepicker.extensions.functions.showError
import com.ravenzip.devicepicker.extensions.functions.showSuccess
import com.ravenzip.devicepicker.state.UiEvent
import com.ravenzip.devicepicker.viewmodels.base.UiEventEffect
import com.ravenzip.devicepicker.viewmodels.user.CompanyViewModel
import com.ravenzip.workshop.components.SnackBar
import com.ravenzip.workshop.components.Spinner

@Composable
fun CompanyScreenScaffold(viewModel: CompanyViewModel = hiltViewModel(), padding: PaddingValues) {
    Scaffold(modifier = Modifier.padding(padding)) { innerPadding ->
        val spinner = viewModel.spinner.collectAsStateWithLifecycle().value

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

        SnackBar(viewModel.snackBarHostState)

        if (spinner.isLoading) {
            Spinner(spinner.text)
        }
    }
}
