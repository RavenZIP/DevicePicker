package com.ravenzip.devicepicker.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ScreenTitle(text: String) {
    Text(
        text = text,
        modifier = Modifier.fillMaxWidth(0.9f),
        fontSize = 25.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.sp,
    )
}

@Composable
fun SmallText(
    text: String,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight = FontWeight.W400,
    textAlign: TextAlign? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = 14.sp,
        fontWeight = fontWeight,
        textAlign = textAlign,
        letterSpacing = letterSpacing,
    )
}

@Composable
fun DefaultText(
    text: String,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight = FontWeight.W400,
) {
    Text(text = text, modifier = modifier, fontWeight = fontWeight)
}

@Composable
fun TextWithIcon(
    modifier: Modifier = Modifier.fillMaxWidth(),
    icon: ImageVector,
    iconSize: Dp = 22.dp,
    text: String,
    fontWeight: FontWeight = FontWeight.W400,
    spacerWidth: Dp = 10.dp,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    smallText: Boolean = false,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = horizontalArrangement,
    ) {
        Icon(
            modifier = Modifier.size(iconSize),
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
        )

        Spacer(modifier = Modifier.width(spacerWidth))

        if (smallText) SmallText(text = text, fontWeight = fontWeight) else DefaultText(text = text)
    }
}
