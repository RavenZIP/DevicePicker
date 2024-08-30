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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ravenzip.devicepicker.constants.enums.TagsEnum
import com.ravenzip.devicepicker.state.DeviceCompactState
import com.ravenzip.devicepicker.state.DeviceCompactState.Companion.listOfCategories
import com.ravenzip.devicepicker.ui.components.ColumnDeviceCard
import com.ravenzip.workshop.components.ChipRadioGroup
import kotlinx.coroutines.flow.StateFlow

@Composable
fun HomeScreen(
    padding: PaddingValues,
    deviceCompactStateByViewModel: StateFlow<DeviceCompactState>,
    getDevice: (uid: String, brand: String, model: String) -> Unit,
    navigateToDevice: () -> Unit,
) {
    val deviceCompactState = deviceCompactStateByViewModel.collectAsState().value
    val listOfCategories = deviceCompactState.listOfCategories()

    Column(
        modifier = Modifier.fillMaxSize().padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ChipRadioGroup(
            list = listOfCategories,
            containerPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth(0.9f).fillMaxHeight(),
            contentPadding = PaddingValues(vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            val selectedCategoryName =
                listOfCategories.firstOrNull { category -> category.isSelected }?.text
            val selectedCategory =
                TagsEnum.entries.firstOrNull { tag -> tag.value == selectedCategoryName }
            val devices = deviceCompactState.categories[selectedCategory] ?: mutableStateListOf()

            items(devices) { device ->
                ColumnDeviceCard(
                    device = device,
                    onClick = {
                        getDevice(device.uid, device.brand, device.model)
                        navigateToDevice()
                    },
                )
            }
        }
    }
}
