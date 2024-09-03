package com.ravenzip.devicepicker.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ravenzip.devicepicker.ui.components.ColumnDeviceCard
import com.ravenzip.devicepicker.viewmodels.HomeScreenViewModel
import com.ravenzip.workshop.components.ChipRadioGroup

@Composable
fun HomeScreen(
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel(),
    padding: PaddingValues,
    navigateToDevice: () -> Unit,
) {
    val categoriesState = homeScreenViewModel.categories.collectAsState().value
    val selectedCategoryState = homeScreenViewModel.selectedCategory.collectAsState().value

    Column(
        modifier = Modifier.fillMaxSize().padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ChipRadioGroup(
            list = categoriesState,
            containerPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
            onClick = { item -> homeScreenViewModel.selectCategory(item) },
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth(0.9f).fillMaxHeight(),
            contentPadding = PaddingValues(vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(selectedCategoryState) { device ->
                ColumnDeviceCard(
                    device = device,
                    onClick = {
                        /// homeScreenViewModel.getDevice(device.uid, device.brand, device.model)
                        navigateToDevice()
                    },
                )
            }
        }
    }
}
