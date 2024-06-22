package com.ravenzip.devicepicker.state

data class SearchBarState(
    val placeholder: String,
    val onSearch: () -> Unit,
) {
    constructor() : this(placeholder = "Введите текст...", onSearch = {})
}
