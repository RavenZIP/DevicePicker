package com.ravenzip.devicepicker.viewmodels

import androidx.lifecycle.ViewModel
import com.ravenzip.devicepicker.enums.TopAppBarTypeEnum
import com.ravenzip.devicepicker.state.SearchBarState
import com.ravenzip.devicepicker.state.TopAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class TopAppBarViewModel @Inject constructor() : ViewModel() {
    private val _type = MutableStateFlow(TopAppBarTypeEnum.TopAppBar)
    private val _topAppBarState = MutableStateFlow(TopAppBarState())
    private val _searchBarState = MutableStateFlow(SearchBarState())

    val type = _type.asStateFlow()
    val topAppBarState = _topAppBarState.asStateFlow()
    val searchBarState = _searchBarState.asStateFlow()

    fun setType(type: TopAppBarTypeEnum) {
        _type.value = type
    }

    fun setTopBarState(topAppBarState: TopAppBarState) {
        _topAppBarState.value = topAppBarState
    }

    fun setSearchBarState(searchBarState: SearchBarState) {
        _searchBarState.value = searchBarState
    }
}
