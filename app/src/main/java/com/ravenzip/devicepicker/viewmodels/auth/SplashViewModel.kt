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

                if (reloadResult.status == StatusEnum.OK) {
                    UiState.Success("Загрузка данных завершена")
                } else {
                    val errorMessage =
                        if (reloadResult.error!!.type == OperationErrorTypeEnum.NETWORK_ERROR)
                            "Ошибка сети. Попробуйте позднее"
                        else "Произошла неизвестная ошибка"

                    UiState.Error(errorMessage)
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = UiState.Loading("Получение данных о пользователе"),
            )
}
