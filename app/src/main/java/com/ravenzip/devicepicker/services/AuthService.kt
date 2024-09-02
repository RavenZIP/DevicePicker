package com.ravenzip.devicepicker.services

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.ravenzip.devicepicker.constants.enums.AuthVariantsEnum
import com.ravenzip.workshop.data.selection.SelectableItemConfig
import javax.inject.Inject

class AuthService @Inject constructor() {
    fun createAuthOptions(): SnapshotStateList<SelectableItemConfig> {
        val email = SelectableItemConfig(isSelected = true, text = AuthVariantsEnum.EMAIL.value)
        val phone = SelectableItemConfig(isSelected = false, text = AuthVariantsEnum.PHONE.value)
        val google = SelectableItemConfig(isSelected = false, text = AuthVariantsEnum.GOOGLE.value)

        return mutableStateListOf(email, phone, google)
    }

    fun calculateSelectedOption(
        authOptions: SnapshotStateList<SelectableItemConfig>
    ): AuthVariantsEnum {
        val selectedVariantString = authOptions.first { value -> value.isSelected }.text
        return AuthVariantsEnum.entries.first { it.value === selectedVariantString }
    }

    fun selectOption(
        item: SelectableItemConfig,
        authOptions: SnapshotStateList<SelectableItemConfig>,
        update: (SnapshotStateList<SelectableItemConfig>) -> Unit,
    ) {
        val updatedAuthOptions = authOptions.toMutableList()
        updatedAuthOptions.replaceAll { it.copy(isSelected = it.text == item.text) }
        update(updatedAuthOptions.toMutableStateList())
    }
}
