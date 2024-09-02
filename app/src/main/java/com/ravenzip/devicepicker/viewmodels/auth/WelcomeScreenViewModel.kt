package com.ravenzip.devicepicker.viewmodels.auth

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.repositories.AuthRepository
import com.ravenzip.devicepicker.services.showError
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class WelcomeScreenViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    private val _alertDialogIsShown = MutableStateFlow(false)

    val isLoading = _isLoading.asStateFlow()
    val alertDialogIsShown = _alertDialogIsShown.asStateFlow()
    val snackBarHostState = SnackbarHostState()

    fun showDialog() {
        _alertDialogIsShown.update { true }
    }

    fun hideDialog() {
        _alertDialogIsShown.update { false }
    }

    fun onDialogConfirmation(navigateToHomeScreen: () -> Unit) {
        viewModelScope.launch {
            _isLoading.update { true }

            val reloadResult = authRepository.reloadUser()
            val logInResult = authRepository.logInAnonymously()

            _isLoading.update { false }
            hideDialog()

            if (logInResult.value == null || reloadResult.value == false) {
                snackBarHostState.showError(logInResult.error!!.message)
            } else {
                navigateToHomeScreen()
            }
        }
    }
}
