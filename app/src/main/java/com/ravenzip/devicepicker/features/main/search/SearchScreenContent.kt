package com.ravenzip.devicepicker.features.main.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravenzip.devicepicker.common.components.RowDeviceCard
import com.ravenzip.devicepicker.common.enums.TagsEnum
import com.ravenzip.devicepicker.common.utils.extension.veryLightPrimary
import com.ravenzip.workshop.components.Icon
import com.ravenzip.workshop.components.VerticalGrid
import com.ravenzip.workshop.data.icon.IconConfig
import com.ravenzip.workshop.data.icon.IconData

@Composable
fun SearchScreenContent(viewModel: SearchViewModel, padding: PaddingValues) {
    val brands = viewModel.brandList.collectAsStateWithLifecycle().value
    val deviceTypes = viewModel.deviceTypeList.collectAsStateWithLifecycle().value

    if (viewModel.wasSearched.value) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            item {
                Text(
                    text = "Выбранная категория поиска: Xiaomi",
                    modifier = Modifier.fillMaxSize(0.9f),
                    fontSize = 18.sp,
                )
            }

            items(viewModel.searchResults) { device -> RowDeviceCard(device = device) }
        }
    } else {

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item { Spacer(modifier = Modifier.height(10.dp)) }

            item {
                Text(
                    text = "Категории устройств",
                    modifier = Modifier.fillMaxSize(0.9f),
                    fontSize = 18.sp,
                )
            }

            item { Spacer(modifier = Modifier.height(10.dp)) }

            item {
                VerticalGrid(items = deviceTypes) { modifier, item -> BrandCard(modifier, item) }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }

            item { Text(text = "Бренды", modifier = Modifier.fillMaxSize(0.9f), fontSize = 18.sp) }

            item { Spacer(modifier = Modifier.height(10.dp)) }

            item { VerticalGrid(items = brands) { modifier, item -> BrandCard(modifier, item) } }

            item { Spacer(modifier = Modifier.height(20.dp)) }

            item { Text(text = "Метки", modifier = Modifier.fillMaxSize(0.9f), fontSize = 18.sp) }

            item { Spacer(modifier = Modifier.height(10.dp)) }

            items(items = TagsEnum.entries, key = { item -> item }) { item ->
                TagCard(item)
                Spacer(modifier = Modifier.height(10.dp))
            }

            item { Spacer(modifier = Modifier.height(10.dp)) }
        }
    }
}

@Composable
private fun BrandCard(modifier: Modifier, brandName: String) {
    Card(
        modifier = modifier.clip(RoundedCornerShape(12.dp)).clickable {},
        colors = CardDefaults.veryLightPrimary(),
    ) {
        Text(
            text = brandName,
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W500,
            letterSpacing = 1.sp,
        )
    }
}

@Composable
private fun TagCard(tag: TagsEnum) {
    Card(
        modifier = Modifier.fillMaxWidth(0.9f).clip(RoundedCornerShape(12.dp)).clickable {},
        colors = CardDefaults.veryLightPrimary(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                icon = IconData.ResourceIcon(tag.icon),
                iconConfig = IconConfig.Small,
                defaultColor = tag.color,
            )

            Text(
                text = tag.value,
                modifier = Modifier.padding(20.dp),
                fontWeight = FontWeight.W500,
                letterSpacing = 1.sp,
            )
        }
    }
}
