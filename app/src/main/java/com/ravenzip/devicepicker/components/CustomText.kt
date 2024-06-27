package com.ravenzip.devicepicker.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravenzip.devicepicker.R
import java.text.DecimalFormat

@Composable
fun CustomText(text: String, size: Int = 14, weight: FontWeight = FontWeight.W400) {
    Text(
        text = text,
        modifier = Modifier.fillMaxWidth(),
        fontSize = size.sp,
        fontWeight = weight,
        textAlign = TextAlign.Start)
}

@Composable
fun Price(price: Int) {
    val pattern = DecimalFormat("###,###,###")
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
