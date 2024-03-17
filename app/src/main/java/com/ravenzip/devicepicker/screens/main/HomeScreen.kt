package com.ravenzip.devicepicker.screens.main

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
import com.ravenzip.devicepicker.data.device.promotions.Promotions
import com.ravenzip.devicepicker.extensions.functions.defaultCardColors
import com.ravenzip.devicepicker.extensions.functions.highestCardColors
import com.ravenzip.devicepicker.extensions.functions.imageContainer
import com.ravenzip.devicepicker.services.DataService

@Composable
fun HomeScreen(padding: PaddingValues, dataService: DataService) {
    val devices = dataService.promotions.collectAsState().value
    val categories = dataService.categories.collectAsState().value

    Column(
        modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Главная",
            modifier = Modifier.fillMaxWidth(0.9f).padding(top = 20.dp, bottom = 20.dp),
            fontSize = 25.sp
        )
        categories.forEach {
            if (it.type == 0) CarouselDevices(devices, it.name)
            else SpecialOfferContainer(devices, it.name)
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun DeviceCard(device: Promotions) {
    Card(
        modifier = Modifier.clip(RoundedCornerShape(12.dp)).clickable {},
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
private fun CarouselDevices(devices: MutableList<Promotions>, categoryName: String) {
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
                    if (it.category.name == categoryName) {
                        DeviceCard(it)
                        Spacer(modifier = Modifier.padding(start = 15.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun SpecialOfferContainer(devices: MutableList<Promotions>, categoryName: String) {
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
                    devices.forEach {
                        if (it.category.name == categoryName) {
                            SpecialOfferCard(devices)
                            Spacer(modifier = Modifier.padding(start = 15.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SpecialOfferCard(devices: MutableList<Promotions>) {
    Card(
        modifier = Modifier.width(300.dp).clip(RoundedCornerShape(12.dp)).clickable {},
        colors = CardDefaults.highestCardColors()
    ) {
        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                bitmap = devices[0].image,
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
                CustomText(text = "${devices[0].price} ₽", size = 16, weight = FontWeight.W500)
                CustomText(text = devices[0].type)
                CustomText(text = devices[0].model)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.i_medal),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.padding(start = 7.5.dp))
                    CustomText(text = "${devices[0].rating} (${devices[0].reviewsCount})")
                }
            }
        }
    }
}
