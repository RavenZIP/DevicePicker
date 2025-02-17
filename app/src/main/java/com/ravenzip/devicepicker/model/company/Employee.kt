package com.ravenzip.devicepicker.model.company

import com.ravenzip.devicepicker.constants.enums.EmployeePosition

data class Employee(val uid: String, val name: String, val position: EmployeePosition) {
    constructor() : this(uid = "", name = "", position = EmployeePosition.Employee)

    companion object {
        fun createEmployee(uid: String, name: String) =
            Employee(uid, name, EmployeePosition.Employee)

        fun createLeader(uid: String, name: String) = Employee(uid, name, EmployeePosition.Leader)
    }
}
