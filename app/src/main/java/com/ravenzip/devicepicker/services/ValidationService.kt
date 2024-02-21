package com.ravenzip.devicepicker.services

import android.util.Patterns.EMAIL_ADDRESS
import android.util.Patterns.PHONE

fun isEmailValid(value: String): Boolean {
    return EMAIL_ADDRESS.matcher(value).matches()
}

fun isPasswordValid(value: String): Boolean{
    return value.length > 5
}

fun isPhoneValid(value: String): Boolean {
    return PHONE.matcher(value).matches()
}

fun isCodeValid(value: String): Boolean{
    return value.length > 5
}