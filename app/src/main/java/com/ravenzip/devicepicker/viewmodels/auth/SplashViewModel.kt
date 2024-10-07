package com.ravenzip.devicepicker.viewmodels.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.ravenzip.devicepicker.constants.enums.OperationErrorTypeEnum
import com.ravenzip.devicepicker.constants.enums.StatusEnum
import com.ravenzip.devicepicker.repositories.AuthRepository
import com.ravenzip.devicepicker.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SplashViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {
    private val _splashScreenState =
        MutableStateFlow<UiState<String>>(UiState.Loading("Получение данных о пользователе"))

    val splashScreenState = _splashScreenState.asStateFlow()

    /**
     * Текущий пользователь firebase
     *
     * @return [FirebaseUser] или null
     */
    val firebaseUser: FirebaseUser?
        get() = authRepository.firebaseUser

    init {
        viewModelScope.launch {
            val reloadResult = authRepository.reloadUser()
            delay(500)

            if (reloadResult.status == StatusEnum.OK) {
                _splashScreenState.update { UiState.Success("Загрузка данных завершена") }
            } else {
                val splashScreenText =
                    if (reloadResult.error!!.type == OperationErrorTypeEnum.NETWORK_ERROR)
                        "Ошибка сети. Попробуйте позднее"
                    else "Произошла неизвестная ошибка"

                _splashScreenState.update { UiState.Error(splashScreenText) }
            }
        }
    }
}
