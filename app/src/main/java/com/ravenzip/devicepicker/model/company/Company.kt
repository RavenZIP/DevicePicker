package com.ravenzip.devicepicker.model.company

data class Company(
    val uid: String,
    val name: String,
    val description: String,
    val address: String,
    val employees: List<Employee>,
    val requestToJoin: List<String>,
    val code: String,
    val settings: List<CompanySettings>,
) {
    constructor() :
        this(
            uid = "",
            name = "",
            description = "",
            address = "",
            employees = emptyList(),
            requestToJoin = emptyList(),
            code = "",
            settings = emptyList(),
        )
}
