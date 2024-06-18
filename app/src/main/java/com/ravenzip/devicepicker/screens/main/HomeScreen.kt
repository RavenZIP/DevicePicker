package com.ravenzip.devicepicker.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.components.CustomText
import com.ravenzip.devicepicker.data.device.compact.DeviceCompact
import com.ravenzip.devicepicker.extensions.functions.defaultCardColors
import com.ravenzip.devicepicker.extensions.functions.highestCardColors
import com.ravenzip.devicepicker.extensions.functions.imageContainer
import com.ravenzip.devicepicker.services.HomeScreenService
import com.ravenzip.devicepicker.viewmodels.DeviceViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    padding: PaddingValues,
    homeScreenService: HomeScreenService,
    deviceViewModel: DeviceViewModel,
    navigateToDevice: () -> Unit
) {
    val popularDevices = homeScreenService.popularDevices.collectAsState().value
    val lowPriceDevices = homeScreenService.lowPriceDevices.collectAsState().value
    val theBestDevices = homeScreenService.theBestDevices.collectAsState().value
    val recentlyViewedDevices = homeScreenService.recentlyViewedDevices.collectAsState().value
    val highPerformanceDevices = homeScreenService.highPerformanceDevices.collectAsState().value

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.height(20.dp))
            CarouselDevices(
                devices = popularDevices,
                categoryName = "Популярные",
                deviceViewModel = deviceViewModel,
                cardClick = navigateToDevice
            )
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
            CarouselDevices(
                devices = lowPriceDevices,
                categoryName = "Низкая цена",
                deviceViewModel = deviceViewModel,
                cardClick = navigateToDevice
            )
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
            if (theBestDevices.size > 0) {
                SpecialOfferContainer(
                    devices = theBestDevices[0],
                    categoryName = "${theBestDevices[0][0].brand}: лучшие устройства",
                    cardClick = navigateToDevice
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        item {
            CarouselDevices(
                devices = highPerformanceDevices,
                categoryName = "Производительные",
                deviceViewModel = deviceViewModel,
                cardClick = navigateToDevice
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            CarouselDevices(
                devices = recentlyViewedDevices,
                categoryName = "Вы недавно смотрели",
                deviceViewModel = deviceViewModel,
                cardClick = navigateToDevice
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            if (theBestDevices.size > 1) {
                SpecialOfferContainer(
                    devices = theBestDevices[1],
                    categoryName = "${theBestDevices[1][0].brand}: лучшие устройства",
                    cardClick = navigateToDevice
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun CarouselDevices(
    devices: MutableList<DeviceCompact>,
    categoryName: String,
    deviceViewModel: DeviceViewModel,
    cardClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth(0.9f), colors = CardDefaults.defaultCardColors()) {
        Column(modifier = Modifier.padding(top = 15.dp, bottom = 15.dp)) {
            Text(
                text = categoryName,
                modifier = Modifier.padding(start = 15.dp, bottom = 10.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.W500
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                item { Spacer(modifier = Modifier.padding(start = 15.dp)) }

                items(devices, key = { it.uid }, contentType = { DeviceCompact::class }) {
                    DeviceCard(device = it, deviceViewModel, cardClick = cardClick)
                    Spacer(modifier = Modifier.padding(start = 15.dp))
                }
            }
        }
    }
}

@Composable
private fun SpecialOfferContainer(
    devices: MutableList<DeviceCompact>,
    categoryName: String,
    cardClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(0.9f).height(500.dp),
        colors = CardDefaults.defaultCardColors()
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(bottom = 15.dp)) {
            Card(
                modifier = Modifier.padding(top = 15.dp, start = 15.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = "Ключевое слово", modifier = Modifier.padding(10.dp), fontSize = 14.sp)
            }

            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
                Text(
                    text = categoryName,
                    modifier = Modifier.padding(start = 15.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W500
                )
                Spacer(modifier = Modifier.padding(top = 10.dp))
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    item { Spacer(modifier = Modifier.padding(start = 15.dp)) }

                    items(devices, key = { it.uid }, contentType = { DeviceCompact::class }) {
                        SpecialOfferCard(device = it, cardClick = cardClick)
                        Spacer(modifier = Modifier.padding(start = 15.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun DeviceCard(
    device: DeviceCompact,
    deviceViewModel: DeviceViewModel,
    cardClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    Card(
        modifier =
            Modifier.clip(RoundedCornerShape(12.dp))
                .clickable {
                    scope.launch {
                        deviceViewModel
                            .getDeviceByBrandAndUid(brand = device.brand, uid = device.uid)
                            .collect { cardClick() }
                    }
                }
                .widthIn(0.dp, 130.dp),
        colors = CardDefaults.highestCardColors()
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
            Image(
                bitmap = device.image,
                contentDescription = null,
                modifier =
                    Modifier.imageContainer(
                        color = Color.White,
                        padding = PaddingValues(vertical = 15.dp, horizontal = 15.dp),
                        width = 80.dp,
                        height = 80.dp
                    )
            )
            Spacer(modifier = Modifier.padding(top = 5.dp))
            CustomText(text = "${device.price} ₽", size = 16, weight = FontWeight.W500)
            CustomText(text = device.model)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.i_medal),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.padding(start = 7.5.dp))
                CustomText(text = "${device.rating} (${device.reviewsCount})")
            }
        }
    }
}

@Composable
private fun SpecialOfferCard(device: DeviceCompact, cardClick: () -> Unit) {
    Card(
        modifier = Modifier.width(300.dp).clip(RoundedCornerShape(12.dp)).clickable { cardClick() },
        colors = CardDefaults.highestCardColors()
    ) {
        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                bitmap = device.image,
                contentDescription = null,
                modifier =
                    Modifier.imageContainer(
                        color = Color.White,
                        padding = PaddingValues(vertical = 15.dp, horizontal = 15.dp),
                        width = 80.dp,
                        height = 80.dp
                    )
            )
            Column(modifier = Modifier.padding(start = 15.dp)) {
                CustomText(text = "${device.price} ₽", size = 16, weight = FontWeight.W500)
                CustomText(text = device.type)
                CustomText(text = device.model)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.i_medal),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.padding(start = 7.5.dp))
                    CustomText(text = "${device.rating} (${device.reviewsCount})")
                }
            }
        }
    }
}
