package com.ravenzip.devicepicker.features.auth.common

import com.ravenzip.devicepicker.common.enums.AuthTypeEnum
import com.ravenzip.workshop.forms.control.FormControl

class AuthForm(
    val authType: FormControl<AuthTypeEnum>,
    val email: FormControl<String>,
    val password: FormControl<String>,
    val phone: FormControl<String>,
    val code: FormControl<String>,
)
