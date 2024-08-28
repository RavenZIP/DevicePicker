package com.ravenzip.devicepicker.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.constants.enums.TagsEnum
import com.ravenzip.devicepicker.extensions.functions.smallImageContainer
import com.ravenzip.devicepicker.extensions.functions.suspendOnClick
import com.ravenzip.devicepicker.extensions.functions.veryLightPrimary
import com.ravenzip.devicepicker.model.device.compact.DeviceCompact
import com.ravenzip.devicepicker.state.DeviceCompactState
import com.ravenzip.devicepicker.state.DeviceCompactState.Companion.listOfCategories
import com.ravenzip.devicepicker.ui.components.Price
import com.ravenzip.devicepicker.ui.components.SmallText
import com.ravenzip.devicepicker.ui.components.TextWithIcon
import com.ravenzip.workshop.components.ChipRadioGroup
import com.ravenzip.workshop.components.Spinner
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.fresco.FrescoImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

@Composable
fun HomeScreen(
    padding: PaddingValues,
    deviceCompactStateByViewModel: StateFlow<DeviceCompactState>,
    onClickToDeviceCard:
        suspend (device: DeviceCompact, changeIsLoading: (isLoading: Boolean) -> Unit) -> Unit,
) {
    val deviceCompactState = deviceCompactStateByViewModel.collectAsState().value
    val isLoading = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
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
                DeviceCard(
                    device = device,
                    changeIsLoading = { isLoading.value = it },
                    coroutineScope = coroutineScope,
                    onClickToDeviceCard = onClickToDeviceCard,
                )
            }
        }
    }

    if (isLoading.value) {
        Spinner(text = "Загрузка...")
    }
}

@Composable
private fun DeviceCard(
    device: DeviceCompact,
    changeIsLoading: (isLoading: Boolean) -> Unit,
    coroutineScope: CoroutineScope,
    onClickToDeviceCard:
        suspend (device: DeviceCompact, changeIsLoading: (isLoading: Boolean) -> Unit) -> Unit,
) {
    Card(
        modifier =
            Modifier.clip(RoundedCornerShape(12.dp)).suspendOnClick(coroutineScope) {
                onClickToDeviceCard(device, changeIsLoading)
            },
        colors = CardDefaults.veryLightPrimary(),
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
            FrescoImage(
                imageUrl = device.imageUrl,
                modifier = Modifier.smallImageContainer(),
                imageOptions = ImageOptions(contentScale = ContentScale.Fit),
            )

            Spacer(modifier = Modifier.padding(top = 5.dp))

            Price(price = device.price)
            SmallText(text = device.model)

            TextWithIcon(
                icon = ImageVector.vectorResource(R.drawable.i_medal),
                iconSize = 14.dp,
                text = "${device.rating} (${device.reviewsCount})",
                spacerWidth = 5.dp,
                smallText = true,
            )
        }
    }
}

@Composable
private fun SpecialOfferCard(
    device: DeviceCompact,
    changeIsLoading: (isLoading: Boolean) -> Unit,
    coroutineScope: CoroutineScope,
    onClickToDeviceCard:
        suspend (device: DeviceCompact, changeIsLoading: (isLoading: Boolean) -> Unit) -> Unit,
) {
    Card(
        modifier =
            Modifier.width(300.dp).clip(RoundedCornerShape(12.dp)).suspendOnClick(coroutineScope) {
                onClickToDeviceCard(device, changeIsLoading)
            },
        colors = CardDefaults.veryLightPrimary(),
    ) {
        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            FrescoImage(
                imageUrl = device.imageUrl,
                modifier = Modifier.smallImageContainer(),
                imageOptions = ImageOptions(contentScale = ContentScale.Fit),
            )

            Column(modifier = Modifier.padding(start = 15.dp)) {
                Price(price = device.price)
                SmallText(text = device.type)
                SmallText(text = device.model)

                TextWithIcon(
                    icon = ImageVector.vectorResource(R.drawable.i_medal),
                    iconSize = 14.dp,
                    text = "${device.rating} (${device.reviewsCount})",
                    spacerWidth = 5.dp,
                    smallText = true,
                )
            }
        }
    }
}
