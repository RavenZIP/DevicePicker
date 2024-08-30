package com.ravenzip.devicepicker.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.extensions.functions.smallImageContainer
import com.ravenzip.devicepicker.extensions.functions.suspendOnClick
import com.ravenzip.devicepicker.extensions.functions.veryLightPrimary
import com.ravenzip.devicepicker.model.device.compact.DeviceCompact
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.fresco.FrescoImage
import kotlinx.coroutines.CoroutineScope

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

            Spacer(modifier = Modifier.padding(top = 5.dp))

            Price(price = device.price)
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
fun RowDeviceCard(
    device: DeviceCompact,
    coroutineScope: CoroutineScope,
    onClickToDeviceCard: suspend () -> Unit,
) {
    Card(
        modifier =
            Modifier.width(300.dp).clip(RoundedCornerShape(12.dp)).suspendOnClick(coroutineScope) {
                onClickToDeviceCard()
            },
        colors = CardDefaults.veryLightPrimary(),
    ) {
        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            FrescoImage(
                imageUrl = device.imageUrl,
                modifier = Modifier.smallImageContainer(),
                imageOptions = ImageOptions(contentScale = ContentScale.Fit),
            )

            Column(modifier = Modifier.padding(start = 15.dp)) {
                Price(price = device.price)
                SmallText(text = device.type)
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
}
