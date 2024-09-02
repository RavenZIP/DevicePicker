package com.ravenzip.devicepicker.state

import com.ravenzip.workshop.data.Error

data class AuthErrorState(
    val email: Error,
    val password: Error,
    val phone: Error,
    val code: Error,
) {
    companion object {
        fun default(): AuthErrorState {
            return AuthErrorState(
                email = Error(),
                password = Error(),
                phone = Error(),
                code = Error(),
            )
        }
    }
}
