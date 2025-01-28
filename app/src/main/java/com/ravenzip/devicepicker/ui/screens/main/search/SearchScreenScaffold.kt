package com.ravenzip.devicepicker.ui.screens.main.search

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.ravenzip.devicepicker.viewmodels.main.SearchViewModel
import com.ravenzip.workshop.components.SearchBar

@Composable
fun SearchScreenScaffold(viewModel: SearchViewModel, padding: PaddingValues) {
    val query = rememberSaveable { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.padding(padding),
        topBar = { SearchBar(query = query, placeholder = "Введите текст...", onSearch = {}) },
    ) { innerPadding ->
        SearchScreenContent(
            brandsState = viewModel.brandList,
            deviceTypesState = viewModel.deviceTypeList,
            innerPadding,
        )
    }
}
