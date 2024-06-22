package com.ravenzip.devicepicker.screens.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.ravenzip.devicepicker.extensions.functions.bigImageContainer
import com.ravenzip.devicepicker.viewmodels.DeviceViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.fresco.FrescoImage

@Composable
fun DeviceInfoScreen(padding: PaddingValues, deviceViewModel: DeviceViewModel) {
    val deviceState = deviceViewModel.deviceState.collectAsState().value
    val device = deviceState.device

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally) {
            if (device.imageUrls.isNotEmpty()) {
                item {
                    FrescoImage(
                        imageUrl = device.imageUrls[0],
                        modifier = Modifier.bigImageContainer(),
                        imageOptions = ImageOptions(contentScale = ContentScale.Fit))
                }
            }

            //            items(device.imageUrls) { imageUrl ->
            //                FrescoImage(
            //                    imageUrl = imageUrl,
            //                    modifier = Modifier.bigImageContainer(),
            //                    imageOptions = ImageOptions(contentScale = ContentScale.Fit))
            //            }
        }
}
