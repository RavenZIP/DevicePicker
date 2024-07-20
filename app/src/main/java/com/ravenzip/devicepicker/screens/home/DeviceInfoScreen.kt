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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.ravenzip.devicepicker.components.ColoredBoxWithBorder
import com.ravenzip.devicepicker.components.PriceRange
import com.ravenzip.devicepicker.components.SmallText
import com.ravenzip.devicepicker.components.TextWithIcon
import com.ravenzip.devicepicker.extensions.functions.bigImageContainer
import com.ravenzip.devicepicker.extensions.functions.veryLightPrimary
import com.ravenzip.devicepicker.map.colorMap
import com.ravenzip.devicepicker.model.ButtonData
import com.ravenzip.devicepicker.model.device.compact.DeviceSpecifications.Companion.toMap
import com.ravenzip.devicepicker.model.device.configurations.PhoneConfiguration
import com.ravenzip.devicepicker.model.device.specifications.Screen.Companion.diagonal
import com.ravenzip.devicepicker.viewmodels.DeviceViewModel
import com.ravenzip.workshop.components.HorizontalPagerIndicator
import com.ravenzip.workshop.components.VerticalGrid
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
                FeedbackContainer(
                    generateFeedbackList(
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
                Specifications(device.specifications.toMap())
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
private fun FeedbackContainer(feedback: List<ButtonData>) {
    Row(
        modifier =
            Modifier.fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(10.dp),
        horizontalArrangement = Arrangement.Center) {
            feedback.forEach { feedback ->
                Button(
                    modifier = Modifier.fillMaxWidth().padding(start = 5.dp, end = 5.dp).weight(1f),
                    onClick = { feedback.onClick() },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.veryLightPrimary(),
                    contentPadding = PaddingValues(5.dp)) {
                        Column(
                            modifier = Modifier.padding(vertical = 0.dp, horizontal = 0.dp),
                            horizontalAlignment = Alignment.CenterHorizontally) {
                                Spacer(modifier = Modifier.padding(top = 5.dp))
                                TextWithIcon(
                                    icon = feedback.icon,
                                    iconSize = 16.dp,
                                    text = feedback.value,
                                    spacerWidth = 5.dp,
                                    horizontalArrangement = Arrangement.Center)

                                Text(text = feedback.text, fontWeight = FontWeight.W400)
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

            VerticalGrid(width = 1f, items = configurations) { modifier, configuration ->
                DeviceConfiguration(modifier, configuration)
            }

            Spacer(modifier = Modifier.height(20.dp))

            TextWithIcon(icon = ImageVector.vectorResource(R.drawable.i_palette), text = "Цвета")

            Spacer(modifier = Modifier.height(15.dp))

            VerticalGrid(width = 1f, items = colors) { modifier, color ->
                DeviceColor(modifier, color)
            }
        }
}

/** Конфигурация устройства */
@Composable
private fun DeviceConfiguration(modifier: Modifier, configuration: PhoneConfiguration) {
    val text = "${configuration.randomAccessMemory}/${configuration.internalMemory}Gb "
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.veryLightPrimary()) {
            Text(
                modifier = Modifier.padding(10.dp).fillMaxWidth(),
                text = text,
                textAlign = TextAlign.Center)
        }
}

/** Цвет устройства */
@Composable
private fun DeviceColor(modifier: Modifier, color: String) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.veryLightPrimary()) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center) {
                    ColoredBoxWithBorder(colorMap[color]!!)
                    Spacer(modifier = Modifier.width(5.dp))

                    Text(
                        modifier = Modifier.weight(1f),
                        text = color,
                        textAlign = TextAlign.Center,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis)
                    Spacer(modifier = Modifier.width(22.dp))
                }
        }
}

@Composable
private fun generateFeedbackList(
    deviceRating: Double,
    deviceReviewsCount: Int,
    deviceQuestionsCount: Int
): List<ButtonData> {
    val rating =
        ButtonData(
            icon = ImageVector.vectorResource(R.drawable.i_medal),
            value = deviceRating.toString(),
            text = "Оценка",
            onClick = {})

    val reviewsCount =
        ButtonData(
            icon = ImageVector.vectorResource(R.drawable.i_comment),
            value = deviceReviewsCount.toString(),
            text = "Отзывы",
            onClick = {})

    val questionsCount =
        ButtonData(
            icon = ImageVector.vectorResource(R.drawable.i_question),
            value = deviceQuestionsCount.toString(),
            text = "Вопросы",
            onClick = {})

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
                    if (specificationCategory.key == "Общая информация") {
                        Spacer(modifier = Modifier.height(10.dp))
                    } else {
                        Spacer(modifier = Modifier.height(30.dp))
                    }

                    SpecificationCategory(specificationCategory)
                }
            }
        }
}

@Composable
fun SpecificationCategory(category: Map.Entry<String, Map<String, String>>) {
    SmallText(text = category.key, modifier = Modifier.fillMaxWidth(), fontWeight = FontWeight.W500)
    Spacer(modifier = Modifier.height(5.dp))

    for (specificationEntries in category.value) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
                SmallText(
                    text = specificationEntries.key,
                    modifier = Modifier.weight(1f),
                    letterSpacing = 0.sp)
                SmallText(
                    text = specificationEntries.value,
                    modifier = Modifier.weight(0.8f),
                    textAlign = TextAlign.End,
                    letterSpacing = 0.sp)
            }
    }
}
