package com.ravenzip.devicepicker.repositories

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.ravenzip.devicepicker.sources.AuthSources
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await

@Singleton
class AuthRepository @Inject constructor(private val authSources: AuthSources) {
    fun getUser(): FirebaseUser? {
        return authSources.currentUser()
    }

    suspend fun reloadUser() {
        getUser()?.reload()?.await()
    }

    suspend fun logInAnonymously(): AuthResult {
        return authSources.auth().signInAnonymously().await()
    }

    suspend fun createUserWithEmail(email: String, password: String): AuthResult? {
        return authSources.auth().createUserWithEmailAndPassword(email, password).await()
    }

    suspend fun logInUserWithEmail(email: String, password: String): AuthResult? {
        return authSources.auth().signInWithEmailAndPassword(email, password).await()
    }

    suspend fun sendEmailVerification() {
        getUser()?.sendEmailVerification()?.await()
    }

    suspend fun isEmailVerified(): Boolean {
        reloadUser()
        return getUser()?.isEmailVerified == true
    }

    suspend fun sendPasswordResetEmail(email: String) {
        authSources.auth().sendPasswordResetEmail(email).await()
    }

    suspend fun logout() {
        authSources.auth().signOut()
        reloadUser()
    }

    suspend fun deleteAccount() {
        getUser()?.delete()?.await()
        reloadUser()
    }
}
