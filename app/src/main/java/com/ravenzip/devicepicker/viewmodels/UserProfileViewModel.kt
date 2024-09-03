package com.ravenzip.devicepicker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.repositories.AuthRepository
import com.ravenzip.devicepicker.repositories.SharedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class UserProfileViewModel
@Inject
constructor(
    private val authRepository: AuthRepository,
    private val sharedRepository: SharedRepository,
) : ViewModel() {
    val userData = sharedRepository.userData

    init {
        viewModelScope.launch { sharedRepository.getUserData() }
    }

    /** Выполнить выход из аккаунта */
    fun logout() {
        authRepository.logout()
    }
}
