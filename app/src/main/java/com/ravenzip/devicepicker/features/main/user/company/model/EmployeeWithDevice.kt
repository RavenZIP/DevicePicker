package com.ravenzip.devicepicker.features.main.user.company.model

import androidx.compose.runtime.Stable

@Stable data class EmployeeWithDevice(val employee: Employee, val devices: List<CompanyDevice>)
