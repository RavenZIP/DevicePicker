package com.ravenzip.devicepicker.viewmodels.auth

import androidx.lifecycle.ViewModel
import com.ravenzip.devicepicker.model.result.Result
import com.ravenzip.devicepicker.repositories.AuthRepository
import com.ravenzip.devicepicker.state.UiEvent
import com.ravenzip.devicepicker.ui.model.AlertDialog
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.zip

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class WelcomeViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {
    val alertDialog = AlertDialog()

    val uiEvent =
        alertDialog.isConfirmed.flatMapLatest {
            return@flatMapLatest authRepository.reloadUserFlow().zip(
                flowOf(authRepository.logInAnonymously())
            ) { reloadResult, logInResult ->
                return@zip if (logInResult is Result.Error) {
                    UiEvent.ShowSnackBar.Error(logInResult.message)
                } else if (reloadResult is Result.Error) {
                    UiEvent.ShowSnackBar.Error(reloadResult.message)
                } else {
                    UiEvent.Navigate.Next
                }
            }
        }

    val isLoading = merge(alertDialog.isConfirmed.map { true }, uiEvent.map { false })
}
