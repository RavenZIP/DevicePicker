package com.ravenzip.devicepicker.features.main.user.company.screens.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.ravenzip.devicepicker.common.model.UiEvent
import com.ravenzip.devicepicker.common.model.UiState
import com.ravenzip.devicepicker.common.utils.extension.showError
import com.ravenzip.devicepicker.common.ErrorScreenCard
import com.ravenzip.devicepicker.common.SpinnerWithoutBlockScreen
import com.ravenzip.devicepicker.navigation.NavigationParams
import com.ravenzip.devicepicker.common.utils.base.UiEventEffect
import com.ravenzip.workshop.components.Spinner
import com.ravenzip.workshop.components.TopAppBar
import com.ravenzip.workshop.data.appbar.BackArrow
import com.ravenzip.workshop.data.icon.IconConfig
import com.ravenzip.workshop.data.icon.IconData
import kotlinx.coroutines.launch

@Composable
fun CompanyInfoScreenScaffold(
    viewModel: CompanyInfoViewModel = hiltViewModel(),
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
    val spinnerState = viewModel.spinner.collectAsStateWithLifecycle().value
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    Scaffold(
        modifier = Modifier.padding(padding),
        topBar = { TopAppBar("Компания", backArrow = backArrow) },
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentAlignment = Alignment.Center,
        ) {
            when (uiState) {
                is UiState.Success ->
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        CompanyInfoScreenContent(viewModel, uiState.data)
                    }

                is UiState.Error -> {
                    ErrorScreenCard(
                        text = "Произошла ошибка",
                        description =
                            "При загрузке данных произошла ошибка: ${uiState.message}. " +
                                "Пожалуйста, попробуйте позже",
                    )
                }

                is UiState.Loading -> {
                    SpinnerWithoutBlockScreen()
                }
            }
        }
    }

    UiEventEffect(viewModel.uiEvent) { event ->
        when (event) {
            is UiEvent.Navigate.ByRoute -> {
                navigationParams.navigateTo(event.route)
            }

            is UiEvent.Navigate.WithoutBackStack -> {
                navigationParams.navigateToWithClearBackStack(event.route)
            }

            is UiEvent.Navigate.Parent -> {
                navigationParams.navigateBackToParent()
            }

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
