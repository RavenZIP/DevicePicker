package com.ravenzip.devicepicker.model.company

import com.ravenzip.devicepicker.constants.enums.EmployeePosition

data class Employee(val uid: String, val position: EmployeePosition) {
    constructor() : this(uid = "", position = EmployeePosition.Employee)

    companion object {
        fun createAdministrator(uid: String) = Employee(uid, EmployeePosition.Administrator)
    }
}
