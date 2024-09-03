package com.ravenzip.devicepicker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.ravenzip.devicepicker.SharedRepository
import com.ravenzip.devicepicker.model.User
import com.ravenzip.devicepicker.model.result.Result
import com.ravenzip.devicepicker.repositories.AuthRepository
import com.ravenzip.devicepicker.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class UserViewModel
@Inject
constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val sharedRepository: SharedRepository,
) : ViewModel() {
    private val _user = MutableStateFlow(User())
    val user = _user.asStateFlow()

    init {
        viewModelScope.launch { getUserData() }
    }

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

    /** Выполнить выход из аккаунта */
    fun logout() {
        authRepository.logout()
    }

    /** Получить данные о текущем пользователе */
    suspend fun getUserData() {
        userRepository.getUserData(firebaseUser?.uid).collect { _user.update { it } }
    }

    suspend fun updateDeviceHistory(deviceHistory: List<String>): Boolean {
        val response = userRepository.updateDeviceHistory(firebaseUser?.uid, deviceHistory)
        _user.update { _user.value.copy(deviceHistory = deviceHistory) }

        return response
    }
}
