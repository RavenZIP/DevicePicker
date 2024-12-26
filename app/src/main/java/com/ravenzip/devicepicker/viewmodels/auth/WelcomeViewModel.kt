package com.ravenzip.devicepicker.viewmodels.auth

import androidx.lifecycle.ViewModel
import com.ravenzip.devicepicker.repositories.AuthRepository
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
                return@zip if (logInResult.value == null || reloadResult.value == false) {
                    UiEvent.ShowSnackBar.Error(logInResult.error!!.message)
                } else {
                    UiEvent.Navigate()
                }
            }
        }

    val isLoading = merge(alertDialog.isConfirmed.map { true }, uiEvent.map { false })
}
