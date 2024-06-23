package com.ravenzip.devicepicker.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.extensions.functions.bigImageContainer
import com.ravenzip.devicepicker.extensions.functions.highestCardColors
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
    val title =
        "${device.type} ${device.model}, " +
            "${device.randomAccessMemory}/${device.internalMemory}Gb " +
            "${device.diagonal}\" ${device.year} ${device.color}"

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                ImageContainer(pagerState, device.imageUrls)
            }

            item {
                Spacer(modifier = Modifier.padding(top = 20.dp))
                Text(
                    text = title,
                    modifier = Modifier.fillMaxWidth(0.9f),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold)
            }

            item {
                Spacer(modifier = Modifier.padding(top = 20.dp))
                UserReviewsContainer(device.rating, device.reviewsCount)
            }
        }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ImageContainer(pagerState: PagerState, imageUrls: List<String>) {
    Column(
        modifier =
            Modifier.fillMaxWidth(0.9f).clip(RoundedCornerShape(10.dp)).background(Color.White)) {
            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxWidth()) {
                FrescoImage(
                    imageUrl = imageUrls[it],
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

@Composable
private fun UserReviewsContainer(rating: Double, reviewsCount: Int) {
    Row(
        modifier =
            Modifier.fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(10.dp)) {
            UserReviewsInfo(
                icon = ImageVector.vectorResource(R.drawable.i_medal_filled),
                count = rating.toString(),
                text = "Оценка")

            Box(modifier = Modifier.weight(1f))

            UserReviewsInfo(
                icon = ImageVector.vectorResource(R.drawable.i_comment),
                count = reviewsCount.toString(),
                text = "Отзывы")

            Box(modifier = Modifier.weight(1f))

            UserReviewsInfo(
                icon = ImageVector.vectorResource(R.drawable.i_question),
                count = reviewsCount.toString(),
                text = "Вопросы")
        }
}

@Composable
private fun UserReviewsInfo(icon: ImageVector, count: String, text: String) {
    Card(
        modifier = Modifier.clip(RoundedCornerShape(10.dp)).clickable {},
        colors = CardDefaults.highestCardColors(0.75f)) {
            Column(
                modifier = Modifier.padding(vertical = 5.dp, horizontal = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier = Modifier.padding(top = 5.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.padding(start = 5.dp))
                        Text(text = count)
                    }
                    Text(text = text)
                    Spacer(modifier = Modifier.padding(top = 5.dp))
                }
        }
}
