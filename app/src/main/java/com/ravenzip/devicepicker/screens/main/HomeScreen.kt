package com.ravenzip.devicepicker.screens.main

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.ravenzip.devicepicker.services.DataService

@Composable
fun HomeScreen(padding: PaddingValues, dataService: DataService) {
    val popularThisWeek = mutableListOf(DeviceCompact())
    val similarDevices = dataService.similarDevices.collectAsState().value
    val companyBestDevices = dataService.companyBestDevices.collectAsState().value
    val lowPrice = mutableListOf(DeviceCompact())
    val unknown = dataService.unknown.collectAsState().value
    val isLoading = dataService.isLoading.collectAsState().value

    Column(
        modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Главная",
            modifier = Modifier.fillMaxWidth(0.9f).padding(top = 20.dp, bottom = 20.dp),
            fontSize = 25.sp
        )

        CarouselDevices(popularThisWeek, "Популярные на этой неделе", isLoading)
        Spacer(modifier = Modifier.height(20.dp))
        CarouselDevices(lowPrice, "Низкая цена", isLoading)
        Spacer(modifier = Modifier.height(20.dp))
        SpecialOfferContainer(companyBestDevices, "Redmi: лучшие устройства", isLoading)
        Spacer(modifier = Modifier.height(20.dp))
        CarouselDevices(lowPrice, "Низкая цена", isLoading)
        Spacer(modifier = Modifier.height(20.dp))
        CarouselDevices(unknown, "Заголовок", isLoading)
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun DeviceCard(device: DeviceCompact) {
    Card(
        modifier = Modifier.clip(RoundedCornerShape(12.dp)).clickable {}.widthIn(0.dp, 130.dp),
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
private fun CarouselDevices(
    devices: MutableList<DeviceCompact>,
    categoryName: String,
    isLoading: Boolean
) {
    Card(modifier = Modifier.fillMaxWidth(0.9f), colors = CardDefaults.defaultCardColors()) {
        Column(modifier = Modifier.padding(top = 15.dp, bottom = 15.dp)) {
            Text(
                text = categoryName,
                modifier = Modifier.padding(start = 15.dp, bottom = 10.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.W500
            )
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.padding(start = 15.dp))
                devices.forEach {
                    DeviceCard(it)
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
    isLoading: Boolean
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
                Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    Spacer(modifier = Modifier.padding(start = 15.dp))
                    Log.d("DEVICES", devices.count().toString())
                    devices.forEach {
                        SpecialOfferCard(it)
                        Spacer(modifier = Modifier.padding(start = 15.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun SpecialOfferCard(device: DeviceCompact) {
    Card(
        modifier = Modifier.width(300.dp).clip(RoundedCornerShape(12.dp)).clickable {},
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
