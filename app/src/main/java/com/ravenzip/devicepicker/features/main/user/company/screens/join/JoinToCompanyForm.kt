package com.ravenzip.devicepicker.features.main.user.company.screens.join

import com.ravenzip.devicepicker.features.main.user.company.model.Company
import com.ravenzip.workshop.forms.control.FormControl

class JoinToCompanyForm(
    val company: FormControl<Company>,
    val leader: FormControl<String>,
    val address: FormControl<String>,
    val code: FormControl<String>,
)
