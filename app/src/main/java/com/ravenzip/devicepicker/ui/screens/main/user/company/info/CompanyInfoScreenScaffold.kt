package com.ravenzip.devicepicker.ui.screens.main.user.company.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.ravenzip.devicepicker.extensions.functions.showError
import com.ravenzip.devicepicker.extensions.functions.veryLightPrimary
import com.ravenzip.devicepicker.navigation.NavigationParams
import com.ravenzip.devicepicker.state.UiEvent
import com.ravenzip.devicepicker.state.UiState
import com.ravenzip.devicepicker.ui.screens.main.user.company.InfoCard2
import com.ravenzip.devicepicker.ui.screens.main.user.company.viewmodel.CompanyInfoViewModel
import com.ravenzip.devicepicker.ui.theme.errorColor
import com.ravenzip.devicepicker.viewmodels.base.UiEventEffect
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
    val company = viewModel.company.collectAsStateWithLifecycle().value

    Scaffold(
        modifier = Modifier.padding(padding),
        topBar = { TopAppBar("Компания", backArrow = backArrow) },
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentAlignment = Alignment.Center,
        ) {
            when (company) {
                is UiState.Success ->
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        CompanyInfoScreenContent(viewModel, company.data)
                    }

                is UiState.Error -> {
                    InfoCard2(
                        text = "Произошла ошибка",
                        description =
                            "При загрузке данных произошла ошибка: ${company.message}. " +
                                "Пожалуйста, попробуйте позже",
                        icon = ImageVector.vectorResource(id = R.drawable.i_error),
                        iconColor = errorColor,
                    )
                }

                is UiState.Loading -> {
                    Card(colors = CardDefaults.veryLightPrimary()) {
                        Box(
                            modifier = Modifier.padding(15.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(50.dp),
                                color = MaterialTheme.colorScheme.primary,
                                strokeWidth = 4.dp,
                            )
                        }
                    }
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
