package com.ravenzip.devicepicker.features.main.device.reviews

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.ravenzip.devicepicker.R
import com.ravenzip.workshop.components.RowIconButton
import com.ravenzip.workshop.components.TopAppBar
import com.ravenzip.workshop.data.appbar.BackArrow
import com.ravenzip.workshop.data.icon.IconConfig
import com.ravenzip.workshop.data.icon.IconData

@Composable
fun ReviewsScreenScaffold(navigateBack: () -> Unit, padding: PaddingValues) {
    val backArrow = remember {
        BackArrow(
            icon = IconData.ResourceIcon(R.drawable.i_back),
            iconConfig = IconConfig.Default,
            onClick = navigateBack,
        )
    }

    Scaffold(
        modifier = Modifier.padding(padding),
        topBar = { TopAppBar(title = "Оценки и отзывы", backArrow = backArrow) },
        floatingActionButton = {
            RowIconButton(
                text = "Написать отзыв",
                icon = IconData.ResourceIcon(R.drawable.i_pencil),
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { innerPadding ->
        ReviewsScreenContent(innerPadding)
    }
}
