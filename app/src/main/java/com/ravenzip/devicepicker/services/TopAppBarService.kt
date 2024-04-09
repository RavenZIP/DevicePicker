package com.ravenzip.devicepicker.services

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class TopAppBarService @Inject constructor() : ViewModel() {
    private val _title = MutableStateFlow("")

    val title = _title.asStateFlow()

    fun setTitle(text: String) {
        _title.value = text
    }
}
