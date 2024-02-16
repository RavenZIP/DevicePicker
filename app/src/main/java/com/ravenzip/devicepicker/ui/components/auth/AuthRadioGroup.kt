package com.ravenzip.devicepicker.ui.components.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravenzip.devicepicker.ui.components.getDefaultColors
import com.ravenzip.workshop.components.RadioGroup
import com.ravenzip.workshop.data.SelectionParameters

@Composable
fun AuthVariants(authVariants: SnapshotStateList<SelectionParameters>, title: String) {
    Card(
        modifier = Modifier.fillMaxWidth(0.9f),
        shape = RoundedCornerShape(10.dp),
        colors = getDefaultColors(),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Spacer(modifier = Modifier.padding(top = 20.dp))
        Text(
            text = title,
            modifier = Modifier.padding(start = 15.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.W500
        )
        Spacer(modifier = Modifier.padding(top = 10.dp))
        RadioGroup(width = 1f, list = authVariants, textSize = 16)
    }
}

fun generateAuthVariants(): SnapshotStateList<SelectionParameters> {
    val email = SelectionParameters(isSelected = true, text = AuthEnum.EMAIL.value)
    val phone = SelectionParameters(isSelected = false, text = AuthEnum.PHONE.value)
    val google = SelectionParameters(isSelected = false, text = AuthEnum.GOOGLE.value)

    return mutableStateListOf(email, phone, google)
}