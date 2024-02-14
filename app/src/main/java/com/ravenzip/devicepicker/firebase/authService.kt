package com.ravenzip.devicepicker.firebase

import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

private val auth: FirebaseAuth = FirebaseAuth.getInstance()

fun getUser(): FirebaseUser? {
    return auth.currentUser
}

suspend fun reloadUser() {
    try {
        auth.currentUser?.reload()?.await()
    } catch (e: Exception) {
        withContext(Dispatchers.Main) { Log.d("ReloadResult", "${e.message}") }
    }
}

suspend fun signInAnonymously(): AuthResult? {
    return try {
        val result = auth.signInAnonymously().await()
        result
    } catch (e: Exception) {
        withContext(Dispatchers.Main) { Log.d("AuthResult", "${e.message}") }
        null
    }
}

suspend fun logout(){
    auth.signOut()
    reloadUser()
}
