package com.ravenzip.devicepicker.viewmodels

import androidx.lifecycle.ViewModel
import com.ravenzip.devicepicker.enums.TopAppBarStateEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class TopAppBarViewModel @Inject constructor() : ViewModel() {
    private val _text = MutableStateFlow("")
    private val _state = MutableStateFlow(TopAppBarStateEnum.TopAppBar)
    private val _onSearch = MutableStateFlow {}

    val text = _text.asStateFlow()
    val state = _state.asStateFlow()
    val onSearch = _onSearch.asStateFlow()

    fun setText(text: String) {
        _text.value = text
    }

    fun setState(state: TopAppBarStateEnum) {
        _state.value = state
    }

    fun setOnSearch(onSearch: () -> Unit) {
        _onSearch.value = onSearch
    }
}