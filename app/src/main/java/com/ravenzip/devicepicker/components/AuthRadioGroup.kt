package com.ravenzip.devicepicker.components

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
import com.ravenzip.devicepicker.enums.AuthVariantsEnum
import com.ravenzip.devicepicker.extensions.functions.defaultCardColors
import com.ravenzip.workshop.components.RadioGroup
import com.ravenzip.workshop.data.SelectionParameters

@Composable
fun AuthVariants(authVariants: SnapshotStateList<SelectionParameters>, title: String) {
    Card(
        modifier = Modifier.fillMaxWidth(0.9f),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.defaultCardColors(),
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
    val email = SelectionParameters(isSelected = true, text = AuthVariantsEnum.EMAIL.value)
    val phone = SelectionParameters(isSelected = false, text = AuthVariantsEnum.PHONE.value)
    val google = SelectionParameters(isSelected = false, text = AuthVariantsEnum.GOOGLE.value)

    return mutableStateListOf(email, phone, google)
}

fun getSelectedVariant(registerVariants: SnapshotStateList<SelectionParameters>): AuthVariantsEnum {
    val selectedVariantString = registerVariants.first { value -> value.isSelected }.text
    return AuthVariantsEnum.entries.first { it.value === selectedVariantString }
}
