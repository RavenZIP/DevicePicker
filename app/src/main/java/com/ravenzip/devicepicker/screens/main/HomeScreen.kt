package com.ravenzip.devicepicker.screens.main

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.components.Price
import com.ravenzip.devicepicker.components.SmallText
import com.ravenzip.devicepicker.components.TextWithIcon
import com.ravenzip.devicepicker.constants.enums.ContainerTypeEnum
import com.ravenzip.devicepicker.extensions.functions.defaultCardColors
import com.ravenzip.devicepicker.extensions.functions.smallImageContainer
import com.ravenzip.devicepicker.extensions.functions.veryLightPrimary
import com.ravenzip.devicepicker.model.device.compact.DeviceCompact
import com.ravenzip.devicepicker.state.DeviceCompactState.Companion.deviceCompactStateToList
import com.ravenzip.devicepicker.viewmodels.DeviceViewModel
import com.ravenzip.devicepicker.viewmodels.ImageViewModel
import com.ravenzip.workshop.components.Spinner
import com.ravenzip.workshop.data.SpinnerState
import com.ravenzip.workshop.data.TextParameters
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.fresco.FrescoImage
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    padding: PaddingValues,
    deviceViewModel: DeviceViewModel,
    imageViewModel: ImageViewModel,
    navigateToDevice: () -> Unit
) {
    val deviceCompactState = deviceViewModel.deviceCompactState.collectAsState().value
    val listOfDeviceCategories = deviceCompactState.deviceCompactStateToList()
    val spinner = remember { mutableStateOf(SpinnerState()) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally) {
            item { Spacer(modifier = Modifier.height(10.dp)) }

            items(listOfDeviceCategories) { category ->
                if (category.containerType === ContainerTypeEnum.Default) {
                    CarouselDevices(
                        devices = category.devices,
                        categoryName = category.categoryName,
                        deviceViewModel = deviceViewModel,
                        imageViewModel = imageViewModel,
                        spinner = spinner,
                        cardClick = navigateToDevice)
                } else {
                    SpecialOfferContainer(
                        devices = category.devices,
                        categoryName = category.categoryName,
                        cardClick = navigateToDevice)
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }

    if (spinner.value.isLoading) {
        Spinner(text = spinner.value.text)
    }
}

@Composable
private fun CarouselDevices(
    devices: List<DeviceCompact>,
    categoryName: String,
    deviceViewModel: DeviceViewModel,
    imageViewModel: ImageViewModel,
    spinner: MutableState<SpinnerState>,
    cardClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth(0.9f), colors = CardDefaults.defaultCardColors()) {
        Column(modifier = Modifier.padding(top = 15.dp, bottom = 15.dp)) {
            Text(
                text = categoryName,
                modifier = Modifier.padding(start = 15.dp, bottom = 10.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.W500)
            LazyRow(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    item { Spacer(modifier = Modifier.padding(start = 15.dp)) }

                    items(devices, key = { it.uid }, contentType = { DeviceCompact::class }) {
                        DeviceCard(
                            device = it,
                            deviceViewModel = deviceViewModel,
                            imageViewModel = imageViewModel,
                            spinner = spinner,
                            navigateToDevice = cardClick)
                        Spacer(modifier = Modifier.padding(start = 15.dp))
                    }
                }
        }
    }
}

@Composable
private fun SpecialOfferContainer(
    devices: List<DeviceCompact>,
    categoryName: String,
    cardClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(0.9f).height(500.dp),
        colors = CardDefaults.defaultCardColors()) {
            Column(modifier = Modifier.fillMaxSize().padding(bottom = 15.dp)) {
                Card(
                    modifier = Modifier.padding(top = 15.dp, start = 15.dp),
                    colors =
                        CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary)) {
                        Text(
                            text = "Ключевое слово",
                            modifier = Modifier.padding(10.dp),
                            fontSize = 14.sp)
                    }

                Column(
                    modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
                        Text(
                            text = categoryName,
                            modifier = Modifier.padding(start = 15.dp),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.W500)
                        Spacer(modifier = Modifier.padding(top = 10.dp))
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center) {
                                item { Spacer(modifier = Modifier.padding(start = 15.dp)) }

                                items(
                                    devices,
                                    key = { it.uid },
                                    contentType = { DeviceCompact::class }) {
                                        SpecialOfferCard(device = it, navigateToDevice = cardClick)
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
    imageViewModel: ImageViewModel,
    spinner: MutableState<SpinnerState>,
    navigateToDevice: () -> Unit
) {
    val scope = rememberCoroutineScope()

    Card(
        modifier =
            Modifier.clip(RoundedCornerShape(12.dp))
                .clickable {
                    scope.launch {
                        onClickDeviceCard(
                            device, deviceViewModel, imageViewModel, spinner, navigateToDevice)
                    }
                }
                .widthIn(0.dp, 130.dp),
        colors = CardDefaults.veryLightPrimary()) {
            Column(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
                FrescoImage(
                    imageUrl = device.imageUrl,
                    modifier = Modifier.smallImageContainer(),
                    imageOptions = ImageOptions(contentScale = ContentScale.Fit))

                Spacer(modifier = Modifier.padding(top = 5.dp))

                Price(price = device.price)
                SmallText(text = device.model)

                TextWithIcon(
                    icon = ImageVector.vectorResource(R.drawable.i_medal),
                    iconSize = 14.dp,
                    text = "${device.rating} (${device.reviewsCount})",
                    spacerWidth = 5.dp,
                    smallText = true)
            }
        }
}

@Composable
private fun SpecialOfferCard(device: DeviceCompact, navigateToDevice: () -> Unit) {
    Card(
        modifier =
            Modifier.width(300.dp).clip(RoundedCornerShape(12.dp)).clickable { navigateToDevice() },
        colors = CardDefaults.veryLightPrimary()) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically) {
                    FrescoImage(
                        imageUrl = device.imageUrl,
                        modifier = Modifier.smallImageContainer(),
                        imageOptions = ImageOptions(contentScale = ContentScale.Fit))

                    Column(modifier = Modifier.padding(start = 15.dp)) {
                        Price(price = device.price)
                        SmallText(text = device.type)
                        SmallText(text = device.model)

                        TextWithIcon(
                            icon = ImageVector.vectorResource(R.drawable.i_medal),
                            iconSize = 14.dp,
                            text = "${device.rating} (${device.reviewsCount})",
                            spacerWidth = 5.dp,
                            smallText = true)
                    }
                }
        }
}

private suspend fun onClickDeviceCard(
    device: DeviceCompact,
    deviceViewModel: DeviceViewModel,
    imageViewModel: ImageViewModel,
    spinner: MutableState<SpinnerState>,
    navigateToDevice: () -> Unit
) {
    spinner.value = SpinnerState(isLoading = true, TextParameters("Загрузка..."))

    val cachedDevice = deviceViewModel.getCachedDevice(device.uid)

    if (cachedDevice == null) {
        deviceViewModel
            .getDeviceByBrandAndUid(brand = device.brand, uid = device.uid)
            .flatMapConcat {
                imageViewModel.getImageUrls(brand = device.brand, model = device.model)
            }
            .collect { deviceViewModel.setImageUrlToDevices(it) }
    }

    spinner.value = SpinnerState()
    navigateToDevice()
}
