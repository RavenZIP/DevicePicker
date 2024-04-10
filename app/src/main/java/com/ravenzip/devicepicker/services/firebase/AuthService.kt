package com.ravenzip.devicepicker.services.firebase

import android.util.Log
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.ravenzip.devicepicker.data.result.Result
import com.ravenzip.devicepicker.enums.AuthErrorsEnum
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
// TODO: В authService доработать использование метода
suspend fun reloadUser(): Result<Boolean> {
    return try {
        auth.currentUser?.reload()?.await()
        Result(value = true, error = null)
    } catch (e: Exception) {
        withContext(Dispatchers.Main) { Log.d("ReloadResult", "${e.message}") }
        Result(value = false, error = "Не удалось обновить данные о пользователе")
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
        val error = AuthErrorsEnum.getErrorMessage(e)
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

        Result(value = null, error = AuthErrorsEnum.ERROR_DEFAULT.value)
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
            Log.d("FirebaseTooManyRequestsException", AuthErrorsEnum.ERROR_TOO_MANY_REQUESTS.value)
        }

        Result(value = null, error = AuthErrorsEnum.ERROR_TOO_MANY_REQUESTS.value)
    } catch (e: FirebaseAuthException) {
        val error = AuthErrorsEnum.getErrorMessage(e)
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

        Result(value = null, error = AuthErrorsEnum.ERROR_DEFAULT.value)
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
            Log.d("FirebaseTooManyRequestsException", AuthErrorsEnum.ERROR_TOO_MANY_REQUESTS.value)
        }

        Result(value = false, error = AuthErrorsEnum.ERROR_TOO_MANY_REQUESTS.value)
    } catch (e: Exception) {
        withContext(Dispatchers.Main) {
            Log.d("Method", "SendEmailVerification")
            Log.d("Exception", "${e.message}")
        }

        Result(value = false, error = AuthErrorsEnum.ERROR_DEFAULT.value)
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

/**
 * Отправить на почту ссылку для сброса пароля
 *
 * @return true - ссылка отправлена
 */
suspend fun sendPasswordResetEmail(email: String): Boolean {
    return try {
        auth.sendPasswordResetEmail(email).await()
        true
    } catch (e: Exception) {
        withContext(Dispatchers.Main) {
            Log.d("Method", "SendPasswordResetEmail")
            Log.d("Exception", "${e.message}")
        }
        false
    }
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
