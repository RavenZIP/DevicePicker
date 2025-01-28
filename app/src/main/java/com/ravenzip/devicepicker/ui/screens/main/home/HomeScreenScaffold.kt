package com.ravenzip.devicepicker.ui.screens.main.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ravenzip.devicepicker.viewmodels.main.HomeViewModel
import com.ravenzip.workshop.components.TopAppBar

@Composable
fun HomeScreenScaffold(
    viewModel: HomeViewModel,
    padding: PaddingValues,
    navigateToDevice: (uid: String) -> Unit,
) {
    Scaffold(modifier = Modifier.padding(padding), topBar = { TopAppBar(title = "Главная") }) {
        innerPadding ->
        HomeScreenContent(
            viewModel = viewModel,
            padding = innerPadding,
            navigateToDevice = navigateToDevice,
        )
    }
}
