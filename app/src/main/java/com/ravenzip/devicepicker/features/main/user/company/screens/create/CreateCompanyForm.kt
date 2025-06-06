package com.ravenzip.devicepicker.features.main.user.company.screens.create

import com.ravenzip.workshop.forms.control.FormControl

class CreateCompanyForm(
    val name: FormControl<String>,
    val description: FormControl<String>,
    val address: FormControl<String>,
    val code: FormControl<String>,
)
