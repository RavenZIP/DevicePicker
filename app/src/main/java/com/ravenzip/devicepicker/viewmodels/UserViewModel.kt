package com.ravenzip.devicepicker.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.ravenzip.devicepicker.constants.enums.AuthErrorsEnum
import com.ravenzip.devicepicker.model.User
import com.ravenzip.devicepicker.model.result.Result
import com.ravenzip.devicepicker.repositories.AuthRepository
import com.ravenzip.devicepicker.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

@HiltViewModel
class UserViewModel
@Inject
constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _user = MutableStateFlow(User())
    val user = _user.asStateFlow()

    /**
     * Получить текущего пользователя
     *
     * @return [FirebaseUser] или null
     */
    fun getUser(): FirebaseUser? {
        return authRepository.getUser()
    }

    /** Обновить данные о пользователе */
    suspend fun reloadUser(): Result<Boolean> {
        return try {
            authRepository.reloadUser()
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
    suspend fun logInAnonymously(): Result<AuthResult> {
        return try {
            val result = authRepository.logInAnonymously()
            Result(value = result, error = null)
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { Log.d("AuthResult", "${e.message}") }
            Result(value = null, error = "Произошла ошибка при выполнении запроса")
        }
    }

    /**
     * Зарегистрировать пользователя с указанной электронной почтой и паролем
     *
     * @return [AuthResult] или null
     */
    suspend fun createUserWithEmail(email: String, password: String): Result<AuthResult> {
        return try {
            val result = authRepository.createUserWithEmail(email, password)
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
            val result = authRepository.logInUserWithEmail(email, password)
            Result(value = result, error = null)
        } catch (e: FirebaseTooManyRequestsException) {
            withContext(Dispatchers.Main) {
                Log.d("Method", "LoginUserWithEmail")
                Log.d(
                    "FirebaseTooManyRequestsException",
                    AuthErrorsEnum.ERROR_TOO_MANY_REQUESTS.value)
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
            authRepository.sendEmailVerification()
            Result(value = true, error = null)
        } catch (e: FirebaseTooManyRequestsException) {
            withContext(Dispatchers.Main) {
                Log.d("Method", "SendEmailVerification")
                Log.d(
                    "FirebaseTooManyRequestsException",
                    AuthErrorsEnum.ERROR_TOO_MANY_REQUESTS.value)
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
        return authRepository.isEmailVerified()
    }

    /**
     * Отправить на почту ссылку для сброса пароля
     *
     * @return true - ссылка отправлена
     */
    suspend fun sendPasswordResetEmail(email: String): Result<Boolean> {
        return try {
            authRepository.sendPasswordResetEmail(email)
            Result(value = true, error = null)
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Log.d("Method", "SendPasswordResetEmail")
                Log.d("Exception", "${e.message}")
            }
            Result(value = false, error = "Ошибка сброса пароля")
        }
    }

    /** Выполнить выход из аккаунта */
    suspend fun logout() {
        authRepository.logout()
    }

    /**
     * Удалить аккаунт
     *
     * @return true - если аккаунт был удален
     */
    suspend fun deleteAccount(): Result<Boolean> {
        return try {
            authRepository.deleteAccount()
            Result(value = true, error = null)
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { Log.d("DeleteAccount", "${e.message}") }
            Result(value = false, error = "Ошибка сброса пароля")
        }
    }

    /** Получить данные о текущем пользователе */
    suspend fun get(currentUser: FirebaseUser?) =
        flow {
                if (currentUser != null) {
                    val user = userRepository.getUser(currentUser.uid)
                    _user.value = user
                    emit(user)
                } else {
                    throw Exception("currentUser is null")
                }
            }
            .catch {
                withContext(Dispatchers.Main) { Log.e("UserService: Get", "${it.message}") }
                emit(User())
            }

    /** Добавить новые данные о пользователе */
    suspend fun add(currentUser: FirebaseUser?) =
        flow {
                if (currentUser !== null) {
                    userRepository.addUser(currentUser.uid)
                    emit(true)
                } else {
                    throw Exception("currentUser is null")
                }
            }
            .catch {
                withContext(Dispatchers.Main) { Log.e("UserService: Add", "${it.message}") }
                emit(false)
            }
}
