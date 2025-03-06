package com.ravenzip.devicepicker.model.company

import androidx.compose.runtime.Stable

@Stable data class EmployeeWithDevice(val employee: Employee, val devices: List<CompanyDevice>)
