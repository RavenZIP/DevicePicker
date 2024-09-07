package com.ravenzip.devicepicker.ui.screens.main.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ravenzip.devicepicker.viewmodels.main.HomeScreenViewModel
import com.ravenzip.workshop.components.TopAppBar

@Composable
fun HomeScreenScaffold(
    homeScreenViewModel: HomeScreenViewModel,
    padding: PaddingValues,
    navigateToDevice: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.padding(padding),
        topBar = { TopAppBar(title = "Главная", backArrow = null, items = listOf()) },
    ) { innerPadding ->
        HomeScreenContent(
            categoriesState = homeScreenViewModel.categories,
            selectedCategoryState = homeScreenViewModel.selectedCategory,
            padding = innerPadding,
            selectCategory = homeScreenViewModel::selectCategory,
            setDeviceQueryParams = homeScreenViewModel::setDeviceQueryParams,
            navigateToDevice = navigateToDevice,
        )
    }
}
