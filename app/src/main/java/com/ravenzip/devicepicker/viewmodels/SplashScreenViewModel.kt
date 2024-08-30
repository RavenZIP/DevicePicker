package com.ravenzip.devicepicker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.constants.enums.OperationErrorTypeEnum
import com.ravenzip.devicepicker.constants.enums.StatusEnum
import com.ravenzip.devicepicker.model.result.Result
import com.ravenzip.devicepicker.repositories.AuthRepository
import com.ravenzip.devicepicker.state.SplashScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SplashScreenViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {
    private val _splashScreenState = MutableStateFlow(Result.default<SplashScreenState>())

    val splashScreenState = _splashScreenState.asStateFlow()

    init {
        viewModelScope.launch {
            _splashScreenState.update {
                Result.loading(
                    SplashScreenState(
                        isAuthorized = false,
                        text = "Получение данных о пользователе",
                    )
                )
            }

            val reloadResult = authRepository.reloadUser()
            delay(500)

            if (reloadResult.status == StatusEnum.OK) {
                _splashScreenState.update {
                    Result.success(
                        SplashScreenState(isAuthorized = true, text = "Загрузка данных завершена")
                    )
                }
            } else {
                val splashScreenText =
                    if (reloadResult.error!!.type == OperationErrorTypeEnum.NETWORK_ERROR)
                        "Ошибка сети. Попробуйте позднее"
                    else "Произошла неизвестная ошибка"

                val splashScreenState =
                    SplashScreenState(isAuthorized = false, text = splashScreenText)

                _splashScreenState.update {
                    Result.networkError(
                        value = splashScreenState,
                        errorMessage = reloadResult.error.message,
                    )
                }
            }
        }
    }
}
