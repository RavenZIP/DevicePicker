package com.ravenzip.devicepicker.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.navigation.models.RootGraph
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashScreenService(dataService: DataService) : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    private val _startDestination = MutableStateFlow(RootGraph.AUTHENTICATION)
    val isLoading = _isLoading.asStateFlow()
    val startDestination = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            val isReloadSuccess = reloadUser().value
            if (isReloadSuccess == true && getUser() !== null) {
                _startDestination.value = RootGraph.MAIN
            }

            // Если юзер авторизован, то при запуске приложения
            // делаем запрос на получение устройств
            if (getUser() !== null) {
                dataService.getPromotions()
            }

            delay(1000)
            _isLoading.value = false
        }
    }
}
