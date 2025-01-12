package com.ravenzip.devicepicker.ui.screens.main.favourites

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ravenzip.devicepicker.viewmodels.main.FavouritesViewModel
import com.ravenzip.workshop.components.TopAppBar

@Composable
fun FavouritesScreenScaffold(
    favouritesViewModel: FavouritesViewModel = hiltViewModel(),
    padding: PaddingValues,
    navigateToDevice: (uid: String) -> Unit,
) {
    Scaffold(modifier = Modifier.padding(padding), topBar = { TopAppBar(title = "Избранное") }) {
        innerPadding ->
        FavouritesScreenContent(
            favouritesViewModel = favouritesViewModel,
            padding = innerPadding,
            navigateToDevice = navigateToDevice,
        )
    }
}
