package com.ravenzip.devicepicker.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.ravenzip.devicepicker.constants.enums.StatusEnum
import com.ravenzip.devicepicker.constants.enums.TagsEnum
import com.ravenzip.devicepicker.constants.map.colorMap
import com.ravenzip.devicepicker.constants.map.specificationCategoriesMap
import com.ravenzip.devicepicker.constants.map.tagsNameMap
import com.ravenzip.devicepicker.extensions.functions.bigImageContainer
import com.ravenzip.devicepicker.extensions.functions.veryLightPrimary
import com.ravenzip.devicepicker.model.ButtonData
import com.ravenzip.devicepicker.model.Tag
import com.ravenzip.devicepicker.model.User
import com.ravenzip.devicepicker.model.device.Device.Companion.createDeviceTitle
import com.ravenzip.devicepicker.model.device.Device.Companion.createListOfTagsIcons
import com.ravenzip.devicepicker.model.device.Device.Companion.createListOfTagsWithIcons
import com.ravenzip.devicepicker.model.device.compact.DeviceSpecifications.Companion.toMap
import com.ravenzip.devicepicker.model.device.configurations.PhoneConfiguration
import com.ravenzip.devicepicker.state.DeviceState
import com.ravenzip.devicepicker.ui.components.ColoredBoxWithBorder
import com.ravenzip.devicepicker.ui.components.PriceRange
import com.ravenzip.devicepicker.ui.components.SmallText
import com.ravenzip.devicepicker.ui.components.TextWithIcon
import com.ravenzip.workshop.components.BoxedChip
import com.ravenzip.workshop.components.BoxedChipGroup
import com.ravenzip.workshop.components.CustomButton
import com.ravenzip.workshop.components.HorizontalPagerIndicator
import com.ravenzip.workshop.components.InfoCard
import com.ravenzip.workshop.components.RowIconButton
import com.ravenzip.workshop.components.Spinner
import com.ravenzip.workshop.components.VerticalGrid
import com.ravenzip.workshop.data.TextConfig
import com.ravenzip.workshop.data.button.ButtonContentConfig
import com.ravenzip.workshop.data.icon.Icon
import com.ravenzip.workshop.data.icon.IconConfig
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.fresco.FrescoImage
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DeviceInfoScreen(
    padding: PaddingValues,
    userDataByViewModel: StateFlow<User>,
    updateDeviceHistory: suspend (List<String>) -> Boolean,
    deviceStateByViewModel: StateFlow<DeviceState>,
) {
    val deviceState = deviceStateByViewModel.collectAsState().value.device

    when (deviceState.status) {
        StatusEnum.LOADING -> {
            Spinner(text = "Загрузка...")
        }

        StatusEnum.OK -> {
            val device = deviceStateByViewModel.collectAsState().value.device.value!!
            val userData = userDataByViewModel.collectAsState().value
            val pagerState = rememberPagerState(pageCount = { device.imageUrls.count() })
            val title = remember { device.createDeviceTitle() }
            val specificationsMap = remember { device.specifications.toMap() }
            val listOfTagsIcons = device.createListOfTagsIcons()
            val sheetState = rememberModalBottomSheetState()
            val tagsSheetIsVisible = remember { mutableStateOf(false) }

            LaunchedEffect(key1 = Unit) {
                if (!userData.deviceHistory.contains(device.uid)) {
                    updateDeviceHistory(userData.deviceHistory + device.uid)
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
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
                                text = "Подробнее о метках",
                                textConfig = TextConfig(size = 16.sp, weight = FontWeight.Medium),
                                icon = Icon.ResourceIcon(id = R.drawable.i_arrow_right),
                                iconConfig = IconConfig.Default,
                                onClick = { tagsSheetIsVisible.value = true },
                            ),
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = title,
                        modifier = Modifier.fillMaxWidth(0.9f),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(15.dp))
                    FeedbackContainer(
                        generateFeedbackList(
                            device.feedback.rating,
                            device.feedback.reviewsCount,
                            device.feedback.questionsCount,
                        )
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(15.dp))
                    PriceAndConfigurations(device.price, device.configurations, device.colors)
                }

                items(specificationsMap.keys.toList()) { categoryKey ->
                    Specifications(
                        category = categoryKey,
                        specifications = specificationsMap[categoryKey]!!,
                    )
                }

                item { Spacer(modifier = Modifier.height(20.dp)) }
            }

            if (tagsSheetIsVisible.value) {
                TagsBottomSheet(
                    tagsSheetIsVisible = tagsSheetIsVisible,
                    sheetState = sheetState,
                    allTagsWithIcons = device.createListOfTagsWithIcons(),
                )
            }
        }

        StatusEnum.ERROR -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.i_error),
                    contentDescription = "",
                    modifier = Modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(text = "При загрузке данных произошла ошибка")
            }
        }

        else -> {
            // DO nothing
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ImageContainer(pagerState: PagerState, imageUrls: List<String>) {
    Column(
        modifier =
            Modifier.fillMaxWidth(0.9f).clip(RoundedCornerShape(10.dp)).background(Color.White)
    ) {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxWidth()) {
            FrescoImage(
                imageUrl = imageUrls[it],
                modifier = Modifier.bigImageContainer(),
                imageOptions = ImageOptions(contentScale = ContentScale.Fit),
            )
        }

        Box(modifier = Modifier) {
            HorizontalPagerIndicator(
                pagerState,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                MaterialTheme.colorScheme.primary,
                height = 10,
                width = 20,
            )
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
        horizontalArrangement = Arrangement.Center,
    ) {
        feedback.forEach { feedback ->
            Button(
                modifier = Modifier.fillMaxWidth().padding(start = 5.dp, end = 5.dp).weight(1f),
                onClick = { feedback.onClick() },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.veryLightPrimary(),
                contentPadding = PaddingValues(5.dp),
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 0.dp, horizontal = 0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.padding(top = 5.dp))
                    TextWithIcon(
                        icon = feedback.icon,
                        iconSize = 16.dp,
                        text = feedback.value,
                        spacerWidth = 5.dp,
                        horizontalArrangement = Arrangement.Center,
                    )

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
    colors: List<String>,
) {
    Column(
        modifier =
            Modifier.fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(10.dp)
    ) {
        PriceRange(price)

        Spacer(modifier = Modifier.height(15.dp))

        TextWithIcon(
            icon = ImageVector.vectorResource(R.drawable.i_configuration),
            text = "Конфигурации",
        )

        Spacer(modifier = Modifier.height(15.dp))

        VerticalGrid(width = 1f, items = configurations) { modifier, configuration ->
            DeviceConfiguration(modifier, configuration)
        }

        Spacer(modifier = Modifier.height(20.dp))

        TextWithIcon(icon = ImageVector.vectorResource(R.drawable.i_palette), text = "Цвета")

        Spacer(modifier = Modifier.height(15.dp))

        VerticalGrid(width = 1f, items = colors) { modifier, color -> DeviceColor(modifier, color) }
    }
}

/** Конфигурация устройства */
@Composable
private fun DeviceConfiguration(modifier: Modifier, configuration: PhoneConfiguration) {
    val text = "${configuration.randomAccessMemory}/${configuration.internalMemory}Gb "
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.veryLightPrimary(),
    ) {
        Text(
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            text = text,
            textAlign = TextAlign.Center,
        )
    }
}

/** Цвет устройства */
@Composable
private fun DeviceColor(modifier: Modifier, color: String) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.veryLightPrimary(),
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            ColoredBoxWithBorder(colorMap[color]!!)
            Spacer(modifier = Modifier.width(5.dp))

            Text(
                modifier = Modifier.weight(1f),
                text = color,
                textAlign = TextAlign.Center,
                softWrap = false,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.width(22.dp))
        }
    }
}

@Composable
private fun generateFeedbackList(
    deviceRating: Double,
    deviceReviewsCount: Int,
    deviceQuestionsCount: Int,
): List<ButtonData> {
    val rating =
        ButtonData(
            icon = ImageVector.vectorResource(R.drawable.i_medal),
            value = deviceRating.toString(),
            text = "Оценка",
            onClick = {},
        )

    val reviewsCount =
        ButtonData(
            icon = ImageVector.vectorResource(R.drawable.i_comment),
            value = deviceReviewsCount.toString(),
            text = "Отзывы",
            onClick = {},
        )

    val questionsCount =
        ButtonData(
            icon = ImageVector.vectorResource(R.drawable.i_question),
            value = deviceQuestionsCount.toString(),
            text = "Вопросы",
            onClick = {},
        )

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
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
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
                colors = CardDefaults.veryLightPrimary(),
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    SmallText(
                        text = specificationEntries.key + ":",
                        fontWeight = FontWeight.W500,
                        modifier = Modifier.weight(1f),
                        letterSpacing = 0.sp,
                    )
                    SmallText(
                        text = specificationEntries.value,
                        modifier = Modifier.padding(start = 10.dp).weight(1f),
                        textAlign = TextAlign.Start,
                        letterSpacing = 0.sp,
                    )
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
) {
    val selectedTag = rememberSaveable { mutableStateOf<Tag?>(null) }

    ModalBottomSheet(
        onDismissRequest = { tagsSheetIsVisible.value = false },
        sheetState = sheetState,
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            this@ModalBottomSheet.AnimatedVisibility(
                visible = selectedTag.value == null,
                enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Bottom),
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(0.95f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    item {
                        InfoCard(
                            width = 0.95f,
                            icon = Icon.ResourceIcon(R.drawable.i_info),
                            iconConfig = IconConfig.PrimaryBig,
                            title = "Описание",
                            text =
                                "Метки - специальные ярлыки, которые кратко описывают устройство. " +
                                    "Они призваны помочь в первичной оценке устройства. " +
                                    "\n\nВсего ${TagsEnum.entries.size} различных меток, " +
                                    "которые вычисляются системой " +
                                    "на основе имеющихся данных об устройстве. " +
                                    "Метки обновляются каждые 24 часа.",
                            colors = CardDefaults.veryLightPrimary(),
                        )
                    }

                    items(allTagsWithIcons) { tag ->
                        Spacer(modifier = Modifier.height(10.dp))
                        CustomButton(
                            width = 0.95f,
                            title = tagsNameMap[tag.name]!!,
                            text = "Нажмите, чтобы получить подробности",
                            icon = tag.icon.icon,
                            iconConfig = tag.icon.config,
                            colors = ButtonDefaults.veryLightPrimary(),
                            onClick = { selectedTag.value = tag },
                        )
                    }

                    item { Spacer(modifier = Modifier.height(40.dp)) }
                }
            }

            this@ModalBottomSheet.AnimatedVisibility(
                visible = selectedTag.value != null,
                enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Bottom),
            ) {
                if (selectedTag.value != null) {
                    Column(
                        modifier = Modifier.fillMaxWidth(0.95f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        TagInfo(tag = selectedTag.value)
                        Spacer(modifier = Modifier.height(10.dp))
                        RowIconButton(
                            width = 0.95f,
                            text = "Вернуться назад",
                            textConfig = TextConfig(size = 16.sp, weight = FontWeight.Medium),
                            icon = Icon.ResourceIcon(R.drawable.i_back),
                            colors = ButtonDefaults.veryLightPrimary(),
                            contentPadding = PaddingValues(12.dp),
                            onClick = { selectedTag.value = null },
                        )
                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun TagInfo(tag: Tag?) {
    if (tag != null) {
        Row(
            modifier = Modifier.fillMaxWidth(0.95f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BoxedChip(icon = tag.icon.icon, iconConfig = tag.icon.config)
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = tagsNameMap[tag.name]!!, fontSize = 18.sp, fontWeight = FontWeight.W500)
        }

        Spacer(modifier = Modifier.height(10.dp))
        InfoCard(
            width = 0.95f,
            icon = Icon.ResourceIcon(id = R.drawable.i_info),
            iconConfig = IconConfig.PrimaryBig,
            title = "Описание",
            text = tag.name.description,
            colors = CardDefaults.veryLightPrimary(),
        )
    }
}
