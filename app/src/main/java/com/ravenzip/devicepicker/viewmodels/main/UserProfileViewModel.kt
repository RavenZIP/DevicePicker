package com.ravenzip.devicepicker.viewmodels.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.repositories.AuthRepository
import com.ravenzip.devicepicker.repositories.SharedRepository
import com.ravenzip.devicepicker.state.UiState
import com.ravenzip.devicepicker.ui.model.AlertDialog
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
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

    init {
        viewModelScope.launch { sharedRepository.getUserData() }
    }

    val logOutState =
        alertDialog.isConfirmed
            .onEach { authRepository.logout() }
            .map { UiState.Dialog.Confirmed() }

    val uiState =
        merge(
                alertDialog.isShown.map { UiState.Dialog.Opened() },
                alertDialog.isHidden.map { UiState.Default() },
                logOutState,
            )
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 0)
}
