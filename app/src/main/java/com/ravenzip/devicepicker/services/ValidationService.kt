package com.ravenzip.devicepicker.services

import android.util.Patterns.EMAIL_ADDRESS
import android.util.Patterns.PHONE
import com.ravenzip.workshop.data.Error
import javax.inject.Inject

class ValidationService @Inject constructor() {
    fun checkEmail(value: String): Error {
        if (value.isEmpty()) {
            return Error(true, "Поле пустое!")
        }

        if (!EMAIL_ADDRESS.matcher(value).matches()) {
            return Error(true, "Введен некорректный формат email адреса!")
        }

        return Error()
    }

    fun checkPassword(value: String): Error {
        if (value.isEmpty()) {
            return Error(true, "Поле пустое!")
        }

        if (value.length < 6) {
            return Error(true, "Введено менее 6 символов!")
        }

        return Error()
    }

    fun isPhoneValid(value: String): Boolean {
        return PHONE.matcher(value).matches()
    }

    fun isCodeValid(value: String): Boolean {
        return value.length > 5
    }
}
