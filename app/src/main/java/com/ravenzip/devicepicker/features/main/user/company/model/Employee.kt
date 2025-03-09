package com.ravenzip.devicepicker.features.main.user.company.model

import androidx.compose.runtime.Stable
import com.ravenzip.devicepicker.common.enums.EmployeePositionEnum

@Stable
data class Employee(
    val uid: String,
    val name: String,
    val position: EmployeePositionEnum,
    val devices: List<String>,
) {
    constructor() :
        this(uid = "", name = "", position = EmployeePositionEnum.Employee, devices = emptyList())

    companion object {
        fun createEmployee(uid: String, name: String) =
            Employee(uid, name, EmployeePositionEnum.Employee, emptyList())

        fun createLeader(uid: String, name: String) =
            Employee(uid, name, EmployeePositionEnum.Leader, emptyList())
    }
}
