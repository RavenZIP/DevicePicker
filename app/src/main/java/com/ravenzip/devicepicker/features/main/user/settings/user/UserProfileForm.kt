package com.ravenzip.devicepicker.features.main.user.settings.user

import com.ravenzip.workshop.forms.control.FormControl

class UserProfileForm(
    val surname: FormControl<String>,
    val name: FormControl<String>,
    val patronymic: FormControl<String>,
    val email: FormControl<String>,
)
