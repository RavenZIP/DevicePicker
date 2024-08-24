package com.ravenzip.devicepicker.ui.screens.auth.common

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.ravenzip.devicepicker.constants.enums.AuthVariantsEnum
import com.ravenzip.workshop.data.selection.SelectionConfig

fun generateAuthVariants(): SnapshotStateList<SelectionConfig> {
    val email = SelectionConfig(isSelected = true, text = AuthVariantsEnum.EMAIL.value)
    val phone = SelectionConfig(isSelected = false, text = AuthVariantsEnum.PHONE.value)
    val google = SelectionConfig(isSelected = false, text = AuthVariantsEnum.GOOGLE.value)

    return mutableStateListOf(email, phone, google)
}

fun getSelectedVariant(registerVariants: SnapshotStateList<SelectionConfig>): AuthVariantsEnum {
    val selectedVariantString = registerVariants.first { value -> value.isSelected }.text
    return AuthVariantsEnum.entries.first { it.value === selectedVariantString }
}
