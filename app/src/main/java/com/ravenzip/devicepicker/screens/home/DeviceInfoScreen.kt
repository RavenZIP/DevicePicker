package com.ravenzip.devicepicker.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.components.PriceRange
import com.ravenzip.devicepicker.components.SmallText
import com.ravenzip.devicepicker.components.TextWithIcon
import com.ravenzip.devicepicker.extensions.functions.bigImageContainer
import com.ravenzip.devicepicker.extensions.functions.surfaceVariant
import com.ravenzip.devicepicker.map.colorMap
import com.ravenzip.devicepicker.model.UserReviewsInfo
import com.ravenzip.devicepicker.model.device.PhoneConfiguration
import com.ravenzip.devicepicker.model.device.compact.DeviceSpecifications.Companion.map
import com.ravenzip.devicepicker.model.device.specifications.Screen.Companion.diagonal
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
        "${device.specifications.baseInfo.type} ${device.specifications.baseInfo.model}, " +
            "${device.configurations[0].randomAccessMemory}/${device.configurations[0].internalMemory}Gb " +
            "${device.specifications.screen.diagonal()} ${device.specifications.baseInfo.year} ${device.colors[0]}"

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
                Spacer(modifier = Modifier.padding(top = 15.dp))
                UserReviewsContainer(
                    generateUserReviewsList(
                        device.feedback.rating,
                        device.feedback.reviewsCount,
                        device.feedback.questionsCount))
            }

            item {
                Spacer(modifier = Modifier.padding(top = 15.dp))
                PriceAndConfigurations(device.price, device.configurations, device.colors)
            }

            item {
                Spacer(modifier = Modifier.padding(top = 15.dp))
                Specifications(device.specifications.map())
            }

            item { Spacer(modifier = Modifier.padding(top = 20.dp)) }
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
private fun UserReviewsContainer(userReviewsInfo: List<UserReviewsInfo>) {
    Row(
        modifier =
            Modifier.fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(10.dp),
        horizontalArrangement = Arrangement.Center) {
            userReviewsInfo.forEach { userReviewsInfo ->
                Button(
                    modifier = Modifier.fillMaxWidth().padding(start = 5.dp, end = 5.dp).weight(1f),
                    onClick = { /*TODO*/ },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.surfaceVariant(),
                    contentPadding = PaddingValues(5.dp)) {
                        Column(
                            modifier = Modifier.padding(vertical = 0.dp, horizontal = 0.dp),
                            horizontalAlignment = Alignment.CenterHorizontally) {
                                Spacer(modifier = Modifier.padding(top = 5.dp))
                                TextWithIcon(
                                    icon = userReviewsInfo.icon,
                                    iconSize = 16.dp,
                                    text = userReviewsInfo.value,
                                    spacerWidth = 5.dp,
                                    horizontalArrangement = Arrangement.Center)

                                Text(text = userReviewsInfo.text, fontWeight = FontWeight.W400)
                                Spacer(modifier = Modifier.padding(top = 5.dp))
                            }
                    }
            }
        }
}

@Composable
private fun PriceAndConfigurations(
    price: Int,
    configurations: List<PhoneConfiguration>,
    colors: List<String>
) {
    Column(
        modifier =
            Modifier.fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(10.dp)) {
            PriceRange(price)

            Spacer(modifier = Modifier.height(15.dp))

            TextWithIcon(
                icon = ImageVector.vectorResource(R.drawable.i_configuration),
                text = "Конфигурации")

            Spacer(modifier = Modifier.height(15.dp))

            DeviceConfigurations(configurations)

            Spacer(modifier = Modifier.height(20.dp))

            TextWithIcon(icon = ImageVector.vectorResource(R.drawable.i_palette), text = "Цвета")

            Spacer(modifier = Modifier.height(15.dp))

            DeviceColors(colors)

            Spacer(modifier = Modifier.height(15.dp))
        }
}

/** Список конфигураций устройства */
@Composable
private fun DeviceConfigurations(configurations: List<PhoneConfiguration>) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        configurations.forEach { configuration ->
            val text = "${configuration.randomAccessMemory}/${configuration.internalMemory}Gb "
            Card(
                modifier = Modifier.fillMaxWidth().padding(start = 5.dp, end = 5.dp).weight(1f),
                shape = RoundedCornerShape(10.dp)) {
                    Text(
                        modifier = Modifier.padding(10.dp).fillMaxWidth(),
                        text = text,
                        textAlign = TextAlign.Center)
                }
        }
    }
}

/** Список цветов устройства */
@Composable
private fun DeviceColors(colors: List<String>) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        colors.forEach { color ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(start = 5.dp, end = 5.dp).weight(1f),
                shape = RoundedCornerShape(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center) {
                            Box(
                                modifier =
                                    Modifier.size(10.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(colorMap[color]!!))

                            Spacer(modifier = Modifier.width(5.dp))

                            Text(
                                modifier = Modifier.fillMaxWidth(0.85f),
                                text = color,
                                textAlign = TextAlign.Center,
                                softWrap = false,
                                overflow = TextOverflow.Ellipsis)
                        }
                }
        }
    }
}

@Composable
private fun generateUserReviewsList(
    deviceRating: Double,
    deviceReviewsCount: Int,
    deviceQuestionsCount: Int
): List<UserReviewsInfo> {
    val rating =
        UserReviewsInfo(
            icon = ImageVector.vectorResource(R.drawable.i_medal),
            value = deviceRating.toString(),
            text = "Оценка")

    val reviewsCount =
        UserReviewsInfo(
            icon = ImageVector.vectorResource(R.drawable.i_comment),
            value = deviceReviewsCount.toString(),
            text = "Отзывы")

    val questionsCount =
        UserReviewsInfo(
            icon = ImageVector.vectorResource(R.drawable.i_question),
            value = deviceQuestionsCount.toString(),
            text = "Вопросы")

    return listOf(rating, reviewsCount, questionsCount)
}

/** Характеристики */
@Composable
private fun Specifications(specificationsMap: Map<String, Map<String, String>>) {
    Column(
        modifier =
            Modifier.fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
            TextWithIcon(
                icon = ImageVector.vectorResource(R.drawable.i_lamp), text = "Характеристики")
            Column(Modifier.fillMaxWidth().padding(10.dp)) {
                for (specificationCategory in specificationsMap.entries) {
                    SpecificationCategory(specificationCategory)
                }
            }
        }
}

@Composable
fun SpecificationCategory(category: Map.Entry<String, Map<String, String>>) {
    Spacer(modifier = Modifier.height(10.dp))
    SmallText(text = category.key, modifier = Modifier.fillMaxWidth(), fontWeight = FontWeight.W500)
    Spacer(modifier = Modifier.height(5.dp))

    for (baseInfoEntries in category.value) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            SmallText(text = baseInfoEntries.key)
            SmallText(text = baseInfoEntries.value)
        }
    }
}
