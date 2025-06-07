package com.ravenzip.devicepicker.features.main.compare

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.common.components.ColumnDeviceCard
import com.ravenzip.devicepicker.common.components.SmallText
import com.ravenzip.devicepicker.common.dummy.REDMI_NOTE_7
import com.ravenzip.devicepicker.common.dummy.SAMSUNG_GALAXY_A25
import com.ravenzip.devicepicker.common.enums.DeviceTypeEnum
import com.ravenzip.devicepicker.common.enums.TagsEnum
import com.ravenzip.devicepicker.common.model.device.compact.DeviceSpecifications
import com.ravenzip.devicepicker.common.model.device.configurations.PhoneConfigurationDto
import com.ravenzip.devicepicker.common.utils.extension.veryLightPrimary
import com.ravenzip.devicepicker.features.main.device.info.DeviceConfiguration
import com.ravenzip.workshop.components.Chip
import com.ravenzip.workshop.components.ChipRadioGroup
import com.ravenzip.workshop.components.IconButton
import com.ravenzip.workshop.data.ChipViewOptions
import com.ravenzip.workshop.data.TextConfig
import com.ravenzip.workshop.data.icon.IconConfig
import com.ravenzip.workshop.data.icon.IconData

// TODO пока просто макет, потом реализовать подтягивание списка сравнения из БД
@Composable
fun CompareScreenContent(viewModel: CompareScreenViewModel, padding: PaddingValues) {
    Column(
        modifier = Modifier.fillMaxSize().padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ChipRadioGroup(
            control = viewModel.selectedListControl,
            source = DeviceTypeEnum.entries,
            viewOptionsProvider = { item ->
                ChipViewOptions(
                    text = "${item.value} ${(item.ordinal + 1) * 2}",
                    textConfig = TextConfig.SmallMedium,
                    icon = IconData.ResourceIcon(id = item.icon),
                    iconConfig = IconConfig(size = 20),
                )
            },
            keySelector = { it },
            containerPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(0.9f).fillMaxHeight(),
            contentPadding = PaddingValues(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        ColumnDeviceCard(
                            SAMSUNG_GALAXY_A25,
                            withCutName = true,
                            isFavourite = false,
                            isComparable = true,
                        ) {}
                        DeviceSlider(selectedNumber = 1, count = 2)
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        ColumnDeviceCard(
                            REDMI_NOTE_7,
                            withCutName = true,
                            isFavourite = false,
                            isComparable = true,
                        ) {}
                        DeviceSlider(selectedNumber = 2, count = 2)
                    }
                }
            }

            item { TagsComparer(SAMSUNG_GALAXY_A25.tags, REDMI_NOTE_7.tags) }

            item {
                ConfigurationsComparer(
                    viewModel.firstDevice.configurations,
                    viewModel.secondDevice.configurations,
                )
            }

            item {
                SpecificationsComparer(
                    viewModel.firstDevice.specifications,
                    viewModel.secondDevice.specifications,
                )
            }
        }
    }
}

@Composable
private fun DeviceSlider(selectedNumber: Int, count: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        IconButton(icon = IconData.ResourceIcon(R.drawable.i_back)) {}

        Text(text = "$selectedNumber из $count", fontSize = 14.sp)

        IconButton(icon = IconData.ResourceIcon(R.drawable.i_arrow_right)) {}
    }
}

@Composable
private fun TagsComparer(first: List<TagsEnum>, second: List<TagsEnum>) {
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.veryLightPrimary(),
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Сравнение меток",
                    fontWeight = FontWeight.W500,
                    fontSize = 20.sp,
                    letterSpacing = 0.sp,
                )
                SmallText(
                    text =
                        "Метки - специальные ярлыки, которые кратко описывают устройство. " +
                            "Они вычисляются на основе имеющихся данных об устройстве " +
                            "(как характеристик, так и отзывов пользователей).",
                    letterSpacing = 0.sp,
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(modifier = Modifier.weight(1f).heightIn(max = 500.dp)) {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(first) { item ->
                            Chip(
                                text = item.value,
                                icon = IconData.ResourceIcon(item.icon),
                                iconConfig = IconConfig(size = 20, color = item.color),
                                withCutText = true,
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier.weight(1f).heightIn(max = 500.dp),
                    contentAlignment = Alignment.CenterEnd,
                ) {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(second) { item ->
                            Chip(
                                text = item.value,
                                icon = IconData.ResourceIcon(item.icon),
                                iconConfig = IconConfig(size = 20, color = item.color),
                                withCutText = true,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ConfigurationsComparer(
    first: List<PhoneConfigurationDto>,
    second: List<PhoneConfigurationDto>,
) {
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.veryLightPrimary(),
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Сравнение конфигураций",
                    fontWeight = FontWeight.W500,
                    fontSize = 20.sp,
                    letterSpacing = 0.sp,
                )
                SmallText(
                    text =
                        "Конфигурация устройства - это доступные варианты этого устройства " +
                            "с некоторыми техническими различиями (процессор, оперативная память, постоянная память). ",
                    letterSpacing = 0.sp,
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(modifier = Modifier.weight(1f).heightIn(max = 500.dp)) {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(first) { item ->
                            DeviceConfiguration(modifier = Modifier, configuration = item)
                        }
                    }
                }

                Box(
                    modifier = Modifier.weight(1f).heightIn(max = 500.dp),
                    contentAlignment = Alignment.CenterEnd,
                ) {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(second) { item ->
                            DeviceConfiguration(modifier = Modifier, configuration = item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SpecificationsComparer(first: DeviceSpecifications, second: DeviceSpecifications) {
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.veryLightPrimary(),
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Сравнение характеристик",
                    fontWeight = FontWeight.W500,
                    fontSize = 20.sp,
                    letterSpacing = 0.sp,
                )
                SmallText(
                    text =
                        "Характеристики — это основные технические параметры устройства: " +
                            "процессор, экран, батарея, камеры и другие компоненты.",
                    letterSpacing = 0.sp,
                )
            }
        }
    }
}
