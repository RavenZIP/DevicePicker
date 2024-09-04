package com.ravenzip.devicepicker.viewmodels.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.repositories.AuthRepository
import com.ravenzip.devicepicker.repositories.SharedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class UserProfileViewModel
@Inject
constructor(
    private val authRepository: AuthRepository,
    private val sharedRepository: SharedRepository,
) : ViewModel() {
    private val _alertDialogIsShown = MutableStateFlow(false)

    val alertDialogIsShown = _alertDialogIsShown.asStateFlow()
    val userData = sharedRepository.userData

    init {
        viewModelScope.launch { sharedRepository.getUserData() }
    }

    fun showDialog() {
        _alertDialogIsShown.update { true }
    }

    fun hideDialog() {
        _alertDialogIsShown.update { false }
    }

    fun onDialogConfirmation(navigateToSplashScreen: () -> Unit) {
        hideDialog()
        authRepository.logout()
        navigateToSplashScreen()
    }
}
