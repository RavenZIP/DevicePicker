package com.ravenzip.devicepicker.features.main.search

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ravenzip.workshop.components.SearchBar

@Composable
fun SearchScreenScaffold(viewModel: SearchViewModel, padding: PaddingValues) {
    Scaffold(
        modifier = Modifier.padding(padding),
        topBar = {
            SearchBar(
                control = viewModel.searchControl,
                placeholder = "Введите текст...",
                onSearch = { viewModel.wasSearched.value = true },
            )
        },
    ) { innerPadding ->
        SearchScreenContent(viewModel = viewModel, innerPadding)
    }
}
