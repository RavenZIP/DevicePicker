package com.ravenzip.devicepicker.model

// Пока что тип для List - String,
// но с большой долей вероятности изменится к моменту полноценной реализации
// Firebase срезает наименования, которые начинаются с is и get
// Поэтому вместо isAdmin просто admin
data class User(
    val admin: Boolean,
    val deviceHistory: List<String>,
    val reviews: List<String>,
    val favourites: List<String>,
    val compares: List<String>,
    val companyUid: String,
) {
    constructor() :
        this(
            admin = false,
            deviceHistory = listOf(),
            reviews = listOf(),
            favourites = listOf(),
            compares = listOf(),
            companyUid = "",
        )
}
