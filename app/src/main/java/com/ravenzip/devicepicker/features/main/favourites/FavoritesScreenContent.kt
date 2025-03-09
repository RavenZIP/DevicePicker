package com.ravenzip.devicepicker.features.main.favourites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravenzip.devicepicker.common.components.RowDeviceCard

@Composable
fun FavouritesScreenContent(
    viewModel: FavouritesViewModel,
    padding: PaddingValues,
    navigateToDevice: (uid: String) -> Unit,
) {
    val favourites = viewModel.favourites.collectAsStateWithLifecycle().value

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        items(favourites) { device ->
            RowDeviceCard(
                device = device,
                isFavourite = true,
                onFavouriteClick = { viewModel.tryToUpdateFavourites(device.uid) },
                onCompareClick = { viewModel.tryToUpdateCompares(device.uid) },
                onCardClick = { navigateToDevice(device.uid) },
            )
        }
    }
}
