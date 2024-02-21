package com.ravenzip.devicepicker.services

import android.util.Log
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

// Текущие данные об авторизации пользователя
private val auth: FirebaseAuth = FirebaseAuth.getInstance()

// Результат запроса
class Result<T>(val value: T?, val error: String?)

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
suspend fun createUserWithEmail(email: String, password: String): Result<AuthResult> {
    return try {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        Result(value = result, error = null)
    } catch (e: FirebaseAuthException) {
        val error = AuthErrors.getErrorMessage(e)
        withContext(Dispatchers.Main) {
            Log.d("Method", "CreateUserWithEmail")
            Log.d("FirebaseAuthException", error)
        }

        Result(value = null, error = error)
    } catch (e: Exception) {
        withContext(Dispatchers.Main) {
            Log.d("Method", "CreateUserWithEmail")
            Log.d("Exception", "${e.message}")
        }

        Result(value = null, error = AuthErrors.ERROR_DEFAULT.value)
    }
}

/**
 * Выполнить вход с указанной электронной почтой и паролем
 *
 * @return [AuthResult] или null
 */
suspend fun logInUserWithEmail(email: String, password: String): Result<AuthResult> {
    return try {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        Result(value = result, error = null)
    } catch (e: FirebaseTooManyRequestsException) {
        withContext(Dispatchers.Main) {
            Log.d("Method", "LoginUserWithEmail")
            Log.d("FirebaseTooManyRequestsException", AuthErrors.ERROR_TOO_MANY_REQUESTS.value)
        }

        Result(value = null, error = AuthErrors.ERROR_TOO_MANY_REQUESTS.value)
    } catch (e: FirebaseAuthException) {
        val error = AuthErrors.getErrorMessage(e)
        withContext(Dispatchers.Main) {
            Log.d("Method", "LoginUserWithEmail")
            Log.d("FirebaseAuthException", error)
        }

        Result(value = null, error = error)
    } catch (e: Exception) {
        withContext(Dispatchers.Main) {
            Log.d("Method", "LoginUserWithEmail")
            Log.d("Exception", "${e.message}")
        }

        Result(value = null, error = AuthErrors.ERROR_DEFAULT.value)
    }
}

/**
 * Отправить ссылку на почту для подтверждения регистрации
 *
 * @return true - если на почту была отправлена ссылка на подтверждение
 */
suspend fun sendEmailVerification(): Result<Boolean> {
    return try {
        auth.currentUser?.sendEmailVerification()?.await()
        Result(value = true, error = null)
    } catch (e: FirebaseTooManyRequestsException) {
        withContext(Dispatchers.Main) {
            Log.d("Method", "SendEmailVerification")
            Log.d("FirebaseTooManyRequestsException", AuthErrors.ERROR_TOO_MANY_REQUESTS.value)
        }

        Result(value = false, error = AuthErrors.ERROR_TOO_MANY_REQUESTS.value)
    } catch (e: Exception) {
        withContext(Dispatchers.Main) {
            Log.d("Method", "SendEmailVerification")
            Log.d("Exception", "${e.message}")
        }

        Result(value = false, error = AuthErrors.ERROR_DEFAULT.value)
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
