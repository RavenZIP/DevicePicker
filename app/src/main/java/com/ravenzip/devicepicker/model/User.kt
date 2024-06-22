package com.ravenzip.devicepicker.model

// Пока что тип для List - String,
// но с большой долей вероятности изменится к моменту полноценной реализации
// Firebase срезает наименования, которые начинаются с is и get
// Поэтому вместо isAdmin просто admin
data class User(val admin: Boolean, val searchHistory: List<String>, val reviews: List<String>) {
    constructor() : this(admin = false, searchHistory = listOf(), reviews = listOf())
}
