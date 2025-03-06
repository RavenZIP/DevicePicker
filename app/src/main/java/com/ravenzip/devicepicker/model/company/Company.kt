package com.ravenzip.devicepicker.model.company

import androidx.compose.runtime.Stable
import com.ravenzip.devicepicker.ui.screens.main.user.company.enum.CompanySettingsEnum

@Stable
data class Company(
    val uid: String,
    val name: String,
    val description: String,
    val address: String,
    val employees: List<Employee>,
    val devices: List<CompanyDevice>,
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
            devices = emptyList(),
            requestToJoin = emptyList(),
            code = "",
            settings = emptyList(),
        )

    companion object {
        fun create(
            uid: String,
            name: String,
            description: String,
            address: String,
            leader: Employee,
            code: String,
        ) =
            Company(
                uid = uid,
                name = name,
                description = description,
                address = address,
                employees = listOf(leader),
                devices = emptyList(),
                requestToJoin = emptyList(),
                code = code,
                settings = createSettings(),
            )

        fun createSettings() =
            listOf(
                CompanySettings(
                    name = "Вход в компанию после подтверждения",
                    code = CompanySettingsEnum.JOIN_AFTER_APPROVE.ordinal,
                    description =
                        "Если эта настройка включена, новый участник не получит " +
                            "полный доступ к компании сразу после подачи заявки.",
                    active = false,
                )
            )
    }
}
