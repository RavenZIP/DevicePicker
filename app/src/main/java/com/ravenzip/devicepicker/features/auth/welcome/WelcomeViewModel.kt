package com.ravenzip.devicepicker.features.auth.welcome

import androidx.lifecycle.ViewModel
import com.ravenzip.devicepicker.common.model.AlertDialog
import com.ravenzip.devicepicker.common.model.UiEvent
import com.ravenzip.devicepicker.common.model.result.Result
import com.ravenzip.devicepicker.common.repositories.AuthRepository
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
