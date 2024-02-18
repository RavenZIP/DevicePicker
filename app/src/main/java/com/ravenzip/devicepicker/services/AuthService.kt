package com.ravenzip.devicepicker.services

import android.util.Log
import com.google.firebase.FirebaseTooManyRequestsException
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

/** Обновить данные о пользователе */
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

/**
 * Отправить ссылку на почту для подтверждения регистрации
 *
 * @return true - если на почту была отправлена ссылка на подтверждение
 */
suspend fun sendEmailVerification(): Boolean {
    return try {
        auth.currentUser?.sendEmailVerification()?.await()
        true
    } catch (e: FirebaseTooManyRequestsException) {
        withContext(Dispatchers.Main) {
            Log.d("EmailVerification", "Слишком часто, попробуйте позднее")
        }
        false
    } catch (e: Exception) {
        withContext(Dispatchers.Main) { Log.d("EmailVerification", "${e.message}") }
        false
    }
}

/**
 * Проверить, что почта подтверждена
 *
 * @return true - если почта подтверждена
 */
suspend fun isEmailVerified(): Boolean {
    reloadUser()
    return auth.currentUser?.isEmailVerified == true
}

/** Выполнить выход из аккаунта */
suspend fun logout() {
    auth.signOut()
    reloadUser()
}

/**
 * Удалить аккаунт
 *
 * @return true - если аккаунт был удален
 */
suspend fun deleteAccount(): Boolean {
    return try {
        auth.currentUser?.delete()?.await()
        reloadUser()
        true
    } catch (e: Exception) {
        withContext(Dispatchers.Main) { Log.d("DeleteAccount", "${e.message}") }
        false
    }
}
