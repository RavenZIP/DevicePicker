package com.ravenzip.devicepicker.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravenzip.devicepicker.R
import java.text.DecimalFormat

private val pattern = DecimalFormat("###,###,###")

@Composable
fun CustomText(
    text: String,
    modifier: Modifier = Modifier,
    size: Int = 14,
    fontWeight: FontWeight = FontWeight.W400
) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = size.sp,
        fontWeight = fontWeight,
        textAlign = TextAlign.Start)
}

// TODO выводить диапазон цен (сейчас выводится конкретная цена)
@Composable
fun PriceRange(price: Int) {
    val formattedPrice = pattern.format(price).replace(",", " ") + " ₽"

    // TODO красить кнопку исходя из цены \ диапазона цен
    Button(
        onClick = { /*TODO*/ },
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = formattedPrice, fontSize = 22.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.width(5.dp))

                Icon(
                    modifier = Modifier.size(22.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.i_right),
                    contentDescription = null)
            }
        }
}

@Composable
fun Price(price: Int) {
    val formattedPrice = pattern.format(price).replace(",", " ") + " ₽"

    Text(text = formattedPrice, fontSize = 16.sp, fontWeight = FontWeight.Bold)
}

@Composable
fun TextWithIcon(
    icon: ImageVector,
    iconSize: Dp = 22.dp,
    text: String,
    fontWeight: FontWeight = FontWeight.W400,
    spacerWidth: Dp = 10.dp
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            modifier = Modifier.size(iconSize),
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary)

        Spacer(modifier = Modifier.width(spacerWidth))

        CustomText(text = text, fontWeight = fontWeight)
    }
}
