package com.ravenzip.devicepicker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.navigation.models.RootGraph
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashScreenViewModel(userViewModel: UserViewModel) : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    private val _startDestination = MutableStateFlow(RootGraph.AUTHENTICATION)
    val isLoading = _isLoading.asStateFlow()
    val startDestination = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            val isReloadSuccess = userViewModel.reloadUser().value
            if (isReloadSuccess == true && userViewModel.getUser() !== null) {
                _startDestination.value = RootGraph.MAIN
            }

            delay(500)
            _isLoading.value = false
        }
    }
}
