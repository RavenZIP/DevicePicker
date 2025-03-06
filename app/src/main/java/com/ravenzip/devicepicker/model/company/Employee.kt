package com.ravenzip.devicepicker.model.company

import androidx.compose.runtime.Stable
import com.ravenzip.devicepicker.constants.enums.EmployeePosition

@Stable
data class Employee(
    val uid: String,
    val name: String,
    val position: EmployeePosition,
    val devices: List<String>,
) {
    constructor() :
        this(uid = "", name = "", position = EmployeePosition.Employee, devices = emptyList())

    companion object {
        fun createEmployee(uid: String, name: String) =
            Employee(uid, name, EmployeePosition.Employee, emptyList())

        fun createLeader(uid: String, name: String) =
            Employee(uid, name, EmployeePosition.Leader, emptyList())
    }
}
