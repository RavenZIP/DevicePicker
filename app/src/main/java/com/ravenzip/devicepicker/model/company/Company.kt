package com.ravenzip.devicepicker.model.company

data class Company(
    val uid: String,
    val name: String,
    val description: String,
    val address: String,
    val employees: List<Employee>,
) {
    constructor() : this(uid = "", name = "", description = "", address = "", employees = listOf())
}
