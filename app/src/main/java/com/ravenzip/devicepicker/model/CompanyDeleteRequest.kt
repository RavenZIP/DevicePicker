package com.ravenzip.devicepicker.model

import com.ravenzip.devicepicker.constants.enums.EmployeePosition
import com.ravenzip.devicepicker.model.company.Employee

data class CompanyDeleteRequest(
    val companyUid: String,
    val employeePosition: EmployeePosition,
    val employees: List<Employee>,
)
