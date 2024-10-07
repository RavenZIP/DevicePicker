package com.ravenzip.devicepicker.viewmodels.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.repositories.AuthRepository
import com.ravenzip.devicepicker.state.UiState
import com.ravenzip.devicepicker.ui.model.AlertDialog
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.zip

@HiltViewModel
class WelcomeScreenViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {
    val alertDialog = AlertDialog()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val logInAnonymouslyState =
        alertDialog.isConfirmed.flatMapLatest {
            return@flatMapLatest flowOf(authRepository.reloadUser()).zip(
                flowOf(authRepository.logInAnonymously())
            ) { reloadResult, logInResult ->
                return@zip if (logInResult.value == null || reloadResult.value == false) {
                    UiState.Error(logInResult.error!!.message)
                } else {
                    UiState.Dialog.Confirmed()
                }
            }
        }

    val uiState =
        merge(
                alertDialog.isShown.map { UiState.Dialog.Opened() },
                alertDialog.isConfirmed.map { UiState.Loading("") },
                alertDialog.isHidden.map { UiState.Nothing() },
                logInAnonymouslyState,
            )
            .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 0)
}
