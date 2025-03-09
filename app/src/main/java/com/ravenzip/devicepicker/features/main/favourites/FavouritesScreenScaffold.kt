package com.ravenzip.devicepicker.features.main.favourites

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ravenzip.workshop.components.TopAppBar

@Composable
fun FavouritesScreenScaffold(
    viewModel: FavouritesViewModel = hiltViewModel(),
    padding: PaddingValues,
    navigateToDevice: (uid: String) -> Unit,
) {
    Scaffold(modifier = Modifier.padding(padding), topBar = { TopAppBar(title = "Избранное") }) {
        innerPadding ->
        FavouritesScreenContent(
            viewModel = viewModel,
            padding = innerPadding,
            navigateToDevice = navigateToDevice,
        )
    }
}
