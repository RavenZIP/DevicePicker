package com.ravenzip.devicepicker.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravenzip.devicepicker.extensions.functions.bigImageContainer
import com.ravenzip.devicepicker.viewmodels.DeviceViewModel
import com.ravenzip.workshop.components.HorizontalPagerIndicator
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.fresco.FrescoImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DeviceInfoScreen(padding: PaddingValues, deviceViewModel: DeviceViewModel) {
    val deviceState = deviceViewModel.deviceState.collectAsState().value
    val device = deviceState.device
    val pagerState = rememberPagerState(pageCount = { device.imageUrls.count() })

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Column(
                    modifier =
                        Modifier.fillMaxWidth(0.9f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)) {
                        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxWidth()) {
                            FrescoImage(
                                imageUrl = device.imageUrls[it],
                                modifier = Modifier.bigImageContainer(),
                                imageOptions = ImageOptions(contentScale = ContentScale.Fit))
                        }

                        Box(modifier = Modifier) {
                            HorizontalPagerIndicator(
                                pagerState,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                MaterialTheme.colorScheme.primary,
                                height = 10,
                                width = 20)
                        }
                    }
            }

            item {
                Spacer(modifier = Modifier.padding(top = 20.dp))
                Text(
                    text = "${device.type} ${device.model}",
                    modifier = Modifier.fillMaxWidth(0.9f),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold)
            }

            item {
                Spacer(modifier = Modifier.padding(top = 10.dp))
                Row(modifier = Modifier.fillMaxWidth(0.9f)) {
                    Text(text = device.rating.toString())
                    Spacer(modifier = Modifier.padding(start = 5.dp))
                    Text(text = "тут звездочки...")
                    Spacer(modifier = Modifier.padding(start = 5.dp))
                    Text(text = "(${device.reviewsCount})")
                }
            }
        }
}
