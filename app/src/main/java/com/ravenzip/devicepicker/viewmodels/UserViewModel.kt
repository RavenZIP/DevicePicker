package com.ravenzip.devicepicker.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.ravenzip.devicepicker.model.User
import com.ravenzip.devicepicker.model.result.Result
import com.ravenzip.devicepicker.repositories.AuthRepository
import com.ravenzip.devicepicker.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class UserViewModel
@Inject
constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _user = MutableStateFlow(User())
    val user = _user.asStateFlow()

    /**
     * Текущий пользователь firebase
     *
     * @return [FirebaseUser] или null
     */
    val firebaseUser: FirebaseUser?
        get() = authRepository.firebaseUser

    /** Обновить данные о пользователе */
    suspend fun reloadUser(): Result<Boolean> {
        return authRepository.reloadUser()
    }

    /**
     * Выполнить анонимный вход
     *
     * @return [AuthResult] или null
     */
    suspend fun logInAnonymously(): Result<AuthResult?> {
        return authRepository.logInAnonymously()
    }

    /**
     * Зарегистрировать пользователя с указанной электронной почтой и паролем
     *
     * @return [AuthResult] или null
     */
    suspend fun createUserWithEmail(email: String, password: String): Result<AuthResult?> {
        return authRepository.createUserWithEmail(email, password)
    }

    /**
     * Выполнить вход с указанной электронной почтой и паролем
     *
     * @return [AuthResult] или null
     */
    suspend fun logInUserWithEmail(email: String, password: String): Result<AuthResult?> {
        return authRepository.logInUserWithEmail(email, password)
    }

    /**
     * Отправить ссылку на почту для подтверждения регистрации
     *
     * @return true - если на почту была отправлена ссылка на подтверждение
     */
    suspend fun sendEmailVerification(): Result<Boolean> {
        return authRepository.sendEmailVerification()
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
        return authRepository.sendPasswordResetEmail(email)
    }

    /** Выполнить выход из аккаунта */
    fun logout() {
        authRepository.logout()
    }

    /**
     * Удалить аккаунт
     *
     * @return true - если аккаунт был удален
     */
    suspend fun deleteAccount(): Result<Boolean> {
        return authRepository.deleteAccount()
    }

    /** Получить данные о текущем пользователе */
    suspend fun getUserData() {
        userRepository.getUserData(firebaseUser?.uid).collect { _user.update { it } }
    }

    /** Добавить новые данные о пользователе */
    suspend fun createUserData(): Boolean {
        return userRepository.createUserData(firebaseUser?.uid)
    }

    suspend fun updateDeviceHistory(deviceHistory: List<String>): Boolean {
        val response = userRepository.updateDeviceHistory(firebaseUser?.uid, deviceHistory)
        _user.update { _user.value.copy(deviceHistory = deviceHistory) }

        return response
    }
}
