package com.ravenzip.devicepicker.services

import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

// Текущие данные об авторизации пользователя
private val auth: FirebaseAuth = FirebaseAuth.getInstance()

/**
 * Получить текущего пользователя
 *
 * @return [FirebaseUser] или null
 */
fun getUser(): FirebaseUser? {
    return auth.currentUser
}

/** Проверить, что вход выполнен */
suspend fun reloadUser() {
    try {
        auth.currentUser?.reload()?.await()
    } catch (e: Exception) {
        withContext(Dispatchers.Main) { Log.d("ReloadResult", "${e.message}") }
    }
}

/**
 * Выполнить анонимный вход
 *
 * @return [AuthResult] или null
 */
suspend fun logInAnonymously(): AuthResult? {
    return try {
        val result = auth.signInAnonymously().await()
        result
    } catch (e: Exception) {
        withContext(Dispatchers.Main) { Log.d("AuthResult", "${e.message}") }
        null
    }
}

/**
 * Зарегистрировать пользователя с указанной электронной почтой и паролем
 *
 * @return [AuthResult] или null
 */
suspend fun createUserWithEmail(email: String, password: String): AuthResult? {
    return try {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        result
    } catch (e: Exception) {
        withContext(Dispatchers.Main) { Log.d("AuthResult", "${e.message}") }
        null
    }
}

/**
 * Выполнить вход с указанной электронной почтой и паролем
 *
 * @return [AuthResult] или null
 */
suspend fun logInUserWithEmail(email: String, password: String): AuthResult? {
    return try {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        result
    } catch (e: Exception) {
        withContext(Dispatchers.Main) { Log.d("AuthResult", "${e.message}") }
        null
    }
}

/** Выполнить выход из аккаунта */
suspend fun logout() {
    auth.signOut()
    reloadUser()
}
