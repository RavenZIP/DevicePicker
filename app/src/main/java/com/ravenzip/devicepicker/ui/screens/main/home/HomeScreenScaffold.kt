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
    homeViewModel: HomeViewModel,
    padding: PaddingValues,
    navigateToDevice: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.padding(padding),
        topBar = { TopAppBar(title = "Главная", backArrow = null, items = listOf()) },
    ) { innerPadding ->
        HomeScreenContent(
            categoriesState = homeViewModel.categories,
            selectedCategoryState = homeViewModel.selectedCategory,
            padding = innerPadding,
            selectCategory = homeViewModel::selectCategory,
            setDeviceQueryParams = homeViewModel::setDeviceQueryParams,
            navigateToDevice = navigateToDevice,
        )
    }
}
