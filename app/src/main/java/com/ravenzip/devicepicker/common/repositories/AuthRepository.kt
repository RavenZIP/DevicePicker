package com.ravenzip.devicepicker.common.repositories

import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.ravenzip.devicepicker.common.enums.AuthErrorsEnum
import com.ravenzip.devicepicker.common.model.result.Result
import com.ravenzip.devicepicker.common.sources.AuthSources
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Singleton
class AuthRepository @Inject constructor(private val authSources: AuthSources) {
    private val firebaseAuth: FirebaseAuth
        get() = authSources.firebaseAuth

    val firebaseUser: FirebaseUser?
        get() = authSources.firebaseUser

    val isAnonymousUser: Boolean
        get() = firebaseUser?.isAnonymous == true

    // TODO если отсутствует интернет и пользователь не авторизован
    // то метод не выкинет ошибку сети
    fun reloadUserFlow() =
        flow<Result<Boolean>> {
                firebaseUser?.reload()?.await()
                emit(Result.Success(true))
            }
            .catch { e ->
                withContext(Dispatchers.Main) { Log.e("reloadUser", "${e.message}") }
                if (e is FirebaseNetworkException) {
                    emit(Result.Error.Network("Не удалось обновить данные о пользователе"))
                } else {
                    emit(Result.Error.Default("Не удалось обновить данные о пользователе"))
                }
            }

    @Deprecated("Перейти на использование reloadUserFlow")
    suspend fun reloadUser(): Result<Boolean> {
        return try {
            firebaseUser?.reload()?.await()
            Result.Success(true)
        } catch (e: FirebaseNetworkException) {
            withContext(Dispatchers.Main) { Log.d("reloadUser", "${e.message}") }
            Result.Error.Network("Не удалось обновить данные о пользователе")
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { Log.d("reloadUser", "${e.message}") }
            Result.Error.Default("Не удалось обновить данные о пользователе")
        }
    }

    suspend fun logInAnonymously(): Result<AuthResult?> {
        return try {
            val result = firebaseAuth.signInAnonymously().await()
            Result.Success(result)
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { Log.d("logInAnonymously", "${e.message}") }
            Result.Error.Default("Произошла ошибка при выполнении запроса")
        }
    }

    suspend fun createUserWithEmail(email: String, password: String): Result<AuthResult?> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Result.Success(result)
        } catch (e: FirebaseAuthException) {
            val error = AuthErrorsEnum.getErrorMessage(e)
            withContext(Dispatchers.Main) {
                Log.d("Method", "CreateUserWithEmail")
                Log.d("FirebaseAuthException", error)
            }

            Result.Error.Default(error)
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Log.d("Method", "CreateUserWithEmail")
                Log.d("Exception", "${e.message}")
            }

            Result.Error.Default(AuthErrorsEnum.ERROR_DEFAULT.value)
        }
    }

    suspend fun logInUserWithEmail(email: String, password: String): Result<AuthResult?> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.Success(result)
        } catch (e: FirebaseTooManyRequestsException) {
            withContext(Dispatchers.Main) {
                Log.d("Method", "LoginUserWithEmail")
                Log.d(
                    "FirebaseTooManyRequestsException",
                    AuthErrorsEnum.ERROR_TOO_MANY_REQUESTS.value,
                )
            }

            Result.Error.Default(AuthErrorsEnum.ERROR_TOO_MANY_REQUESTS.value)
        } catch (e: FirebaseAuthException) {
            val error = AuthErrorsEnum.getErrorMessage(e)
            withContext(Dispatchers.Main) {
                Log.d("Method", "LoginUserWithEmail")
                Log.d("FirebaseAuthException", error)
            }

            Result.Error.Default(error)
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Log.d("Method", "LoginUserWithEmail")
                Log.d("Exception", "${e.message}")
            }

            Result.Error.Default(AuthErrorsEnum.ERROR_DEFAULT.value)
        }
    }

    suspend fun sendEmailVerification(): Result<Boolean> {
        return try {
            firebaseUser?.sendEmailVerification()?.await()
            Result.Success(true)
        } catch (e: FirebaseTooManyRequestsException) {
            withContext(Dispatchers.Main) {
                Log.d("Method", "SendEmailVerification")
                Log.d(
                    "FirebaseTooManyRequestsException",
                    AuthErrorsEnum.ERROR_TOO_MANY_REQUESTS.value,
                )
            }

            Result.Error.Default(AuthErrorsEnum.ERROR_TOO_MANY_REQUESTS.value)
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Log.d("Method", "SendEmailVerification")
                Log.d("Exception", "${e.message}")
            }

            Result.Error.Default(AuthErrorsEnum.ERROR_DEFAULT.value)
        }
    }

    suspend fun isEmailVerified(): Boolean {
        reloadUser()
        return firebaseUser?.isEmailVerified == true
    }

    suspend fun sendPasswordResetEmail(email: String): Result<Boolean> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Result.Success(true)
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Log.d("Method", "SendPasswordResetEmail")
                Log.d("Exception", "${e.message}")
            }
            Result.Error.Default("Ошибка сброса пароля")
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    suspend fun deleteAccount(): Result<Boolean> {
        return try {
            firebaseUser?.delete()?.await()
            reloadUser()
            Result.Success(true)
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { Log.d("DeleteAccount", "${e.message}") }
            Result.Error.Default("Ошибка удаления аккаунта")
        }
    }
}
