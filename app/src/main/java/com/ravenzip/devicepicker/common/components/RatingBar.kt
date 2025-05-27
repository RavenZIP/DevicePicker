package com.ravenzip.devicepicker.common.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.ravenzip.devicepicker.R
import com.smarttoolfactory.ratingbar.RatingBar
import com.smarttoolfactory.ratingbar.model.GestureStrategy

@Composable
fun RatingBar(rating: Float, itemSize: Int = 18) {
    RatingBar(
        rating = rating,
        imageVectorEmpty = ImageVector.vectorResource(R.drawable.i_medal),
        imageVectorFilled = ImageVector.vectorResource(R.drawable.i_medal),
        tintEmpty = MaterialTheme.colorScheme.primary.copy(0.5f),
        tintFilled = MaterialTheme.colorScheme.primary,
        gestureStrategy = GestureStrategy.None,
        itemSize = itemSize.dp,
    ) {}
}
