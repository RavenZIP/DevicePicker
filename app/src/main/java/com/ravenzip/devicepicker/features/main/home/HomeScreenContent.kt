package com.ravenzip.devicepicker.features.main.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravenzip.devicepicker.common.components.ColumnDeviceCard
import com.ravenzip.devicepicker.common.enums.TagsEnum
import com.ravenzip.workshop.components.ChipRadioGroup
import com.ravenzip.workshop.data.ChipViewOptions
import com.ravenzip.workshop.data.TextConfig
import com.ravenzip.workshop.data.icon.IconConfig
import com.ravenzip.workshop.data.icon.IconData

@Composable
fun HomeScreenContent(
    viewModel: HomeViewModel,
    padding: PaddingValues,
    navigateToDevice: (uid: String) -> Unit,
) {
    val devices = viewModel.devicesInSelectedCategory.collectAsStateWithLifecycle().value

    Column(
        modifier = Modifier.fillMaxSize().padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ChipRadioGroup(
            control = viewModel.selectedCategoryControl,
            source = TagsEnum.entries,
            viewOptionsProvider = { item ->
                ChipViewOptions(
                    text = item.value,
                    textConfig = TextConfig.SmallMedium,
                    icon = IconData.ResourceIcon(id = item.icon),
                    iconConfig = IconConfig(size = 20, color = item.color),
                )
            },
            keySelector = { it },
            containerPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
        )

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth(0.9f).fillMaxHeight(),
            contentPadding = PaddingValues(vertical = 20.dp),
            verticalItemSpacing = 10.dp,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(devices) { device ->
                ColumnDeviceCard(device = device, onClick = { navigateToDevice(device.uid) })
            }
        }
    }
}
