package com.ravenzip.devicepicker.ui.screens.home

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.ravenzip.devicepicker.constants.enums.TagsEnum
import com.ravenzip.devicepicker.constants.map.colorMap
import com.ravenzip.devicepicker.constants.map.specificationCategoriesMap
import com.ravenzip.devicepicker.constants.map.tagsNameMap
import com.ravenzip.devicepicker.extensions.functions.bigImageContainer
import com.ravenzip.devicepicker.extensions.functions.veryLightPrimary
import com.ravenzip.devicepicker.model.ButtonData
import com.ravenzip.devicepicker.model.Tag
import com.ravenzip.devicepicker.model.Tags.Companion.createListOfChipIcons
import com.ravenzip.devicepicker.model.Tags.Companion.createListOfUniqueTags
import com.ravenzip.devicepicker.model.device.Device.Companion.createDeviceTitle
import com.ravenzip.devicepicker.model.device.compact.DeviceSpecifications.Companion.toMap
import com.ravenzip.devicepicker.model.device.configurations.PhoneConfiguration
import com.ravenzip.devicepicker.state.DeviceState
import com.ravenzip.devicepicker.ui.components.ColoredBoxWithBorder
import com.ravenzip.devicepicker.ui.components.PriceRange
import com.ravenzip.devicepicker.ui.components.SmallText
import com.ravenzip.devicepicker.ui.components.TextWithIcon
import com.ravenzip.workshop.components.BoxedChip
import com.ravenzip.workshop.components.BoxedChipGroup
import com.ravenzip.workshop.components.HorizontalPagerIndicator
import com.ravenzip.workshop.components.VerticalGrid
import com.ravenzip.workshop.data.ButtonContentConfig
import com.ravenzip.workshop.data.IconParameters
import com.ravenzip.workshop.data.TextParameters
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.fresco.FrescoImage
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DeviceInfoScreen(padding: PaddingValues, deviceStateByViewModel: StateFlow<DeviceState>) {
    val device = deviceStateByViewModel.collectAsState().value.device
    val pagerState = rememberPagerState(pageCount = { device.imageUrls.count() })
    val title = remember { device.createDeviceTitle() }
    val specificationsMap = remember { device.specifications.toMap() }
    val allTags = remember { device.tags.createListOfUniqueTags() }
    val allTagsWithIcons = createListOfChipIcons(allTags = allTags)
    val listOfTagsIcons = remember { allTagsWithIcons.map { tag -> tag.icon } }
    val sheetState = rememberModalBottomSheetState()
    val tagsSheetIsVisible = remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                ImageContainer(pagerState, device.imageUrls)
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                BoxedChipGroup(
                    items = listOfTagsIcons,
                    buttonContentConfig =
                        ButtonContentConfig(
                            text = TextParameters("Подробнее о метках"),
                            icon =
                                IconParameters(
                                    value =
                                        ImageVector.vectorResource(id = R.drawable.i_arrow_right)),
                            onClick = { tagsSheetIsVisible.value = true }))
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = title,
                    modifier = Modifier.fillMaxWidth(0.9f),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold)
            }

            item {
                Spacer(modifier = Modifier.height(15.dp))
                FeedbackContainer(
                    generateFeedbackList(
                        device.feedback.rating,
                        device.feedback.reviewsCount,
                        device.feedback.questionsCount))
            }

            item {
                Spacer(modifier = Modifier.height(15.dp))
                PriceAndConfigurations(device.price, device.configurations, device.colors)
            }

            items(specificationsMap.keys.toList()) { categoryKey ->
                Specifications(
                    category = categoryKey, specifications = specificationsMap[categoryKey]!!)
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }

    if (tagsSheetIsVisible.value) {
        TagsBottomSheet(
            tagsSheetIsVisible = tagsSheetIsVisible,
            sheetState = sheetState,
            allTagsWithIcons = allTagsWithIcons,
            computedTags = device.tags.computedTags,
            manualTags = device.tags.manualTags)
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
                .padding(horizontal = 5.dp, vertical = 10.dp),
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
private fun Specifications(category: String, specifications: Map<String, String>) {
    val specificationCategoryIcon =
        ImageVector.vectorResource(specificationCategoriesMap[category]!!)

    Spacer(modifier = Modifier.padding(top = 15.dp))
    Column(
        modifier =
            Modifier.fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
            TextWithIcon(icon = specificationCategoryIcon, text = category)

            Spacer(modifier = Modifier.height(10.dp))

            SpecificationCategory(specifications)
        }
}

@Composable
private fun SpecificationCategory(category: Map<String, String>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        for (specificationEntries in category.entries) {
            Spacer(modifier = Modifier.height(5.dp))

            Card(
                Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.veryLightPrimary()) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically) {
                            SmallText(
                                text = specificationEntries.key + ":",
                                fontWeight = FontWeight.W500,
                                modifier = Modifier.weight(1f),
                                letterSpacing = 0.sp)
                            SmallText(
                                text = specificationEntries.value,
                                modifier = Modifier.padding(start = 10.dp).weight(1f),
                                textAlign = TextAlign.Start,
                                letterSpacing = 0.sp)
                        }
                }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TagsBottomSheet(
    tagsSheetIsVisible: MutableState<Boolean>,
    sheetState: SheetState,
    allTagsWithIcons: List<Tag>,
    computedTags: List<TagsEnum>,
    manualTags: List<TagsEnum>
) {
    ModalBottomSheet(
        onDismissRequest = { tagsSheetIsVisible.value = false }, sheetState = sheetState) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.95f),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                        allTagsWithIcons.forEach { tag ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically) {
                                    BoxedChip(icon = tag.icon, backgroundColor = Color.Transparent)
                                    Text(
                                        text = tagsNameMap[tag.name]!!,
                                        letterSpacing = 0.sp,
                                        fontWeight = FontWeight.W500)
                                }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
            }
        }
}
