package com.ravenzip.devicepicker.features.main.user.company.model

import com.ravenzip.devicepicker.common.enums.EmployeePositionEnum

data class CompanyDeleteRequest(
    val companyUid: String,
    val employeePosition: EmployeePositionEnum,
    val employees: List<Employee>,
)
