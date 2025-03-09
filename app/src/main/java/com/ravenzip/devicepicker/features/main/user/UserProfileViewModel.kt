package com.ravenzip.devicepicker.features.main.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.common.model.AlertDialog
import com.ravenzip.devicepicker.common.model.UiEvent
import com.ravenzip.devicepicker.common.repositories.AuthRepository
import com.ravenzip.devicepicker.common.repositories.SharedRepository
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

    private val logoutWithDelay =
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
