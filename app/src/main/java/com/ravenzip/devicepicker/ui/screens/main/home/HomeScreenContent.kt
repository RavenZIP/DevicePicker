package com.ravenzip.devicepicker.ui.screens.main.home

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravenzip.devicepicker.constants.enums.TagsEnum
import com.ravenzip.devicepicker.constants.map.tagIconMap
import com.ravenzip.devicepicker.constants.map.tagsColorMap
import com.ravenzip.devicepicker.ui.components.ColumnDeviceCard
import com.ravenzip.devicepicker.viewmodels.main.HomeViewModel
import com.ravenzip.workshop.components.ChipRadioGroup
import com.ravenzip.workshop.data.ChipViewOptions
import com.ravenzip.workshop.data.TextConfig
import com.ravenzip.workshop.data.icon.Icon
import com.ravenzip.workshop.data.icon.IconConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    homeViewModel: HomeViewModel,
    padding: PaddingValues,
    navigateToDevice: (uid: String) -> Unit,
) {
    val devices = homeViewModel.devicesInSelectedCategory.collectAsStateWithLifecycle().value

    Column(
        modifier = Modifier.fillMaxSize().padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ChipRadioGroup(
            state = homeViewModel.selectedCategory,
            source = TagsEnum.entries,
            viewOptions =
                TagsEnum.entries.associate { item ->
                    item to
                        ChipViewOptions(
                            text = item.value,
                            textConfig = TextConfig.SmallMedium,
                            icon = Icon.ResourceIcon(id = tagIconMap[item]!!),
                            iconConfig = IconConfig(size = 20, color = tagsColorMap[item]),
                        )
                },
            comparableKey = { it },
            containerPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth(0.9f).fillMaxHeight(),
            contentPadding = PaddingValues(vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(devices) { device ->
                ColumnDeviceCard(device = device, onClick = { navigateToDevice(device.uid) })
            }
        }
    }
}
