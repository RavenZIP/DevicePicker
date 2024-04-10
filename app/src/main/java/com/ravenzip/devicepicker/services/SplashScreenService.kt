package com.ravenzip.devicepicker.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.navigation.models.RootGraph
import com.ravenzip.devicepicker.services.firebase.getUser
import com.ravenzip.devicepicker.services.firebase.reloadUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashScreenService : ViewModel() {
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

            delay(500)
            _isLoading.value = false
        }
    }
}
