package com.ravenzip.devicepicker.sources

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthSources @Inject constructor() {
    private val firebaseAuth = FirebaseAuth.getInstance()

    fun auth(): FirebaseAuth {
        return firebaseAuth
    }

    fun currentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }
}
