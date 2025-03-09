package com.ravenzip.devicepicker.features.auth.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.ravenzip.devicepicker.common.model.UiState
import com.ravenzip.devicepicker.common.model.result.Result
import com.ravenzip.devicepicker.common.repositories.AuthRepository
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
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UiState.Loading("Получение данных о пользователе"),
            )
}
