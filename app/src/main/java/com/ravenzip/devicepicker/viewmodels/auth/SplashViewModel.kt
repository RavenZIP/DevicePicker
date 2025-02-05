package com.ravenzip.devicepicker.viewmodels.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.ravenzip.devicepicker.model.result.Result
import com.ravenzip.devicepicker.repositories.AuthRepository
import com.ravenzip.devicepicker.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class SplashViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {

    /**
     * Текущий пользователь firebase
     *
     * @return [FirebaseUser] или null
     */
    val firebaseUser: FirebaseUser?
        get() = authRepository.firebaseUser

    val uiState =
        authRepository
            .reloadUserFlow()
            .map { reloadResult ->
                delay(500)

                when (reloadResult) {
                    is Result.Success -> {
                        UiState.Success("Загрузка данных завершена")
                    }

                    is Result.Error.Network -> {
                        UiState.Error("Ошибка сети. Попробуйте позднее")
                    }

                    is Result.Error.Default -> {
                        UiState.Error("Произошла неизвестная ошибка")
                    }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = UiState.Loading("Получение данных о пользователе"),
            )
}
