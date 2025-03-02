package com.ravenzip.devicepicker.viewmodels.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.repositories.AuthRepository
import com.ravenzip.devicepicker.repositories.SharedRepository
import com.ravenzip.devicepicker.state.UiEvent
import com.ravenzip.devicepicker.ui.model.AlertDialog
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class UserProfileViewModel
@Inject
constructor(
    private val authRepository: AuthRepository,
    private val sharedRepository: SharedRepository,
) : ViewModel() {
    val alertDialog = AlertDialog()
    val userData = sharedRepository.userData

    val logoutWithDelay =
        alertDialog.isConfirmed.onEach {
            authRepository.logout()
            delay(500)
        }

    val uiEvent = logoutWithDelay.map { UiEvent.Navigate.Next }

    init {
        // TODO добавить обработку ошибок
        viewModelScope.launch { sharedRepository.loadUserData().collect() }
    }
}
