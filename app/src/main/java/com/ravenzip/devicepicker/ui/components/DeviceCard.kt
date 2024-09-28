package com.ravenzip.devicepicker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.ravenzip.devicepicker.extensions.functions.smallImageContainer
import com.ravenzip.devicepicker.extensions.functions.veryLightPrimary
import com.ravenzip.devicepicker.model.device.compact.DeviceCompact
import com.ravenzip.devicepicker.model.device.compact.DeviceCompactExtended
import com.ravenzip.workshop.components.BoxedChip
import com.ravenzip.workshop.components.IconButton
import com.ravenzip.workshop.components.RowIconButton
import com.ravenzip.workshop.data.icon.Icon
import com.ravenzip.workshop.data.icon.IconConfig
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.fresco.FrescoImage
import com.smarttoolfactory.ratingbar.RatingBar
import com.smarttoolfactory.ratingbar.model.GestureStrategy

/** Карточка устройства, с вертикальным расположением данных */
@Composable
fun ColumnDeviceCard(device: DeviceCompact, onClick: () -> Unit) {
    Card(
        modifier = Modifier.clip(RoundedCornerShape(12.dp)).clickable { onClick() },
        colors = CardDefaults.veryLightPrimary(),
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
            FrescoImage(
                imageUrl = device.imageUrl,
                modifier = Modifier.smallImageContainer(),
                imageOptions = ImageOptions(contentScale = ContentScale.Fit),
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = device.price.currentFormatted,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
            SmallText(text = device.model)

            TextWithIcon(
                icon = ImageVector.vectorResource(R.drawable.i_medal),
                iconSize = 14.dp,
                text = "${device.rating} (${device.reviewsCount})",
                spacerWidth = 5.dp,
                smallText = true,
            )
        }
    }
}

/** Карточка устройства, с горизонтальным расположением данных */
@Composable
fun RowDeviceCard(device: DeviceCompactExtended, onClick: () -> Unit) {
    Card(
        modifier =
            Modifier.fillMaxWidth(0.9f).clip(RoundedCornerShape(12.dp)).clickable { onClick() },
        colors = CardDefaults.veryLightPrimary(),
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                FrescoImage(
                    imageUrl = device.imageUrl,
                    modifier = Modifier.smallImageContainer(width = 100.dp, height = 150.dp),
                    imageOptions = ImageOptions(contentScale = ContentScale.Fit),
                )

                DeviceInfoContainer {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = device.model,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 0.sp,
                            lineHeight = 18.sp,
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Card(
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.veryLightPrimary(),
                            ) {
                                Text(
                                    modifier = Modifier.padding(5.dp),
                                    text = device.type,
                                    fontSize = 14.sp,
                                    letterSpacing = 0.sp,
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Card(
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.veryLightPrimary(),
                            ) {
                                Text(
                                    modifier = Modifier.padding(5.dp),
                                    text = "${device.diagonal}\"",
                                    fontSize = 14.sp,
                                    letterSpacing = 0.sp,
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            items(device.tags) { tag ->
                                BoxedChip(icon = tag.icon, iconConfig = tag.config)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.veryLightPrimary(),
                        ) {
                            Row(
                                modifier = Modifier.padding(5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Spacer(modifier = Modifier.width(5.dp))

                                RatingBar(
                                    rating = device.rating.toFloat(),
                                    imageVectorEmpty =
                                        ImageVector.vectorResource(R.drawable.i_medal),
                                    imageVectorFilled =
                                        ImageVector.vectorResource(R.drawable.i_medal),
                                    tintEmpty = MaterialTheme.colorScheme.primary.copy(0.5f),
                                    tintFilled = MaterialTheme.colorScheme.primary,
                                    gestureStrategy = GestureStrategy.None,
                                    itemSize = 18.dp,
                                ) {}

                                Spacer(modifier = Modifier.width(5.dp))

                                Text(
                                    text = device.reviewsCount.toString(),
                                    fontSize = 14.sp,
                                    letterSpacing = 0.sp,
                                )

                                Spacer(modifier = Modifier.width(5.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(5.dp))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(icon = Icon.ResourceIcon(R.drawable.i_compare)) {}

                Spacer(modifier = Modifier.width(10.dp))

                IconButton(icon = Icon.ResourceIcon(R.drawable.i_heart)) {}

                Spacer(modifier = Modifier.weight(1f))

                RowIconButton(
                    width = null,
                    text = device.price.currentFormatted,
                    icon = Icon.ResourceIcon(R.drawable.i_info),
                    iconConfig = IconConfig.Small,
                    iconPositionIsLeft = false,
                    contentPadding = PaddingValues(10.dp),
                )
            }
        }
    }
}

@Composable
fun DeviceInfoContainer(content: @Composable () -> Unit) {
    Column(
        modifier =
            Modifier.padding(start = 10.dp)
                .clip(RoundedCornerShape(10.dp))
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary.copy(0.05f))
                .height(180.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        content()
    }
}
