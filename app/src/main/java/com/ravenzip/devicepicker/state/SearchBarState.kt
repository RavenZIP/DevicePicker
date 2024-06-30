package com.ravenzip.devicepicker.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

data class SearchBarState(
    val placeholder: String,
    val onSearch: () -> Unit,
) {
    constructor() : this(placeholder = "Введите текст...", onSearch = {})

    companion object {
        @Composable
        fun createSearchBarState(
            placeholder: String = "Введите текст...",
            onSearch: () -> Unit = {},
        ): SearchBarState {
            val rememberedOnSearch = remember { { onSearch() } }
            return SearchBarState(placeholder = placeholder, onSearch = rememberedOnSearch)
        }
    }
}
