package com.ravenzip.devicepicker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
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

            Text(text = device.type, fontWeight = FontWeight.W500)
            Text(text = device.model, fontSize = 14.sp, fontWeight = FontWeight.W500)

            Spacer(modifier = Modifier.height(5.dp))

            Specifications(device.diagonal, device.cpu, device.camera, device.battery)

            Spacer(modifier = Modifier.height(5.dp))

            SimpleRatingWithReviewsCount(device.rating, device.reviewsCount)
        }
    }
}

/** Карточка устройства, с горизонтальным расположением данных */
@Composable
fun RowDeviceCard(
    device: DeviceCompactExtended,
    isFavourite: Boolean = false,
    onFavouriteClick: () -> Unit,
    onCompareClick: () -> Unit,
    onCardClick: () -> Unit,
) {
    Card(
        modifier =
            Modifier.fillMaxWidth(0.9f).clip(RoundedCornerShape(12.dp)).clickable { onCardClick() },
        colors = CardDefaults.veryLightPrimary(),
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
            VerticalCenterRow {
                FrescoImage(
                    imageUrl = device.imageUrl,
                    modifier = Modifier.smallImageContainer(width = 100.dp, height = 170.dp),
                    imageOptions = ImageOptions(contentScale = ContentScale.Fit),
                )

                DeviceInfoContainer {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(text = device.type, fontSize = 18.sp, fontWeight = FontWeight.W500)
                        Text(text = device.model, fontSize = 16.sp, fontWeight = FontWeight.W500)

                        Spacer(modifier = Modifier.height(10.dp))

                        Specifications(device.diagonal, device.cpu, device.camera, device.battery)

                        Spacer(modifier = Modifier.height(10.dp))

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            items(device.tags) { tag ->
                                BoxedChip(icon = tag.icon, iconConfig = tag.config)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        RatingWithReviewsCount(device.rating, device.reviewsCount)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            VerticalCenterRow {
                IconButton(icon = Icon.ResourceIcon(R.drawable.i_compare)) { onCompareClick() }

                Spacer(modifier = Modifier.width(10.dp))

                IconButton(
                    icon =
                        Icon.ResourceIcon(
                            if (isFavourite) R.drawable.i_heart_filled else R.drawable.i_heart
                        )
                ) {
                    onFavouriteClick()
                }

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
private fun SimpleRatingWithReviewsCount(rating: Double, reviewsCount: Int) {
    Card(shape = RoundedCornerShape(10.dp), colors = CardDefaults.veryLightPrimary()) {
        TextWithIcon(
            modifier = Modifier.padding(5.dp),
            icon = ImageVector.vectorResource(R.drawable.i_medal),
            iconSize = 16.dp,
            text = "$rating (${reviewsCount})",
            spacerWidth = 5.dp,
            smallText = true,
        )
    }
}

@Composable
private fun RatingWithReviewsCount(rating: Double, reviewsCount: Int) {
    Card(shape = RoundedCornerShape(10.dp), colors = CardDefaults.veryLightPrimary()) {
        Row(
            modifier = Modifier.padding(5.dp).horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RatingBar(
                rating = rating.toFloat(),
                imageVectorEmpty = ImageVector.vectorResource(R.drawable.i_medal),
                imageVectorFilled = ImageVector.vectorResource(R.drawable.i_medal),
                tintEmpty = MaterialTheme.colorScheme.primary.copy(0.5f),
                tintFilled = MaterialTheme.colorScheme.primary,
                gestureStrategy = GestureStrategy.None,
                itemSize = 18.dp,
            ) {}

            Spacer(modifier = Modifier.width(5.dp))

            Text(text = "$rating (${reviewsCount})", fontSize = 14.sp, letterSpacing = 0.sp)
        }
    }
}

@Composable
private fun Specifications(diagonal: Double, cpu: String, camera: Int, battery: Int) {
    VerticalCenterRow(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        Card(shape = RoundedCornerShape(10.dp), colors = CardDefaults.veryLightPrimary()) {
            Text(
                modifier = Modifier.padding(5.dp),
                text = "${diagonal}\"",
                fontSize = 14.sp,
                letterSpacing = 0.sp,
            )
        }

        Spacer(modifier = Modifier.width(5.dp))

        Card(shape = RoundedCornerShape(10.dp), colors = CardDefaults.veryLightPrimary()) {
            Text(
                modifier = Modifier.padding(5.dp),
                text = cpu,
                fontSize = 14.sp,
                letterSpacing = 0.sp,
            )
        }

        Spacer(modifier = Modifier.width(5.dp))

        Card(shape = RoundedCornerShape(10.dp), colors = CardDefaults.veryLightPrimary()) {
            Text(
                modifier = Modifier.padding(5.dp),
                text = "$camera Мп",
                fontSize = 14.sp,
                letterSpacing = 0.sp,
            )
        }

        Spacer(modifier = Modifier.width(5.dp))

        Card(shape = RoundedCornerShape(10.dp), colors = CardDefaults.veryLightPrimary()) {
            Text(
                modifier = Modifier.padding(5.dp),
                text = "$battery мАч",
                fontSize = 14.sp,
                letterSpacing = 0.sp,
            )
        }
    }
}

@Composable
private fun DeviceInfoContainer(content: @Composable () -> Unit) {
    Column(
        modifier =
            Modifier.padding(start = 10.dp)
                .clip(RoundedCornerShape(10.dp))
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary.copy(0.05f)),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        content()
    }
}
