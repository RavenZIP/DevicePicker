package com.ravenzip.devicepicker.features.main.compare

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.ravenzip.devicepicker.R
import com.ravenzip.workshop.components.TopAppBarWithMenu
import com.ravenzip.workshop.data.appbar.AppBarMenuItem
import com.ravenzip.workshop.data.icon.IconConfig
import com.ravenzip.workshop.data.icon.IconData

@Composable
fun CompareScreenScaffold(
    viewModel: CompareScreenViewModel = hiltViewModel(),
    padding: PaddingValues,
) {
    Scaffold(
        modifier = Modifier.padding(padding),
        topBar = {
            TopAppBarWithMenu(
                title = "Сравнение",
                items =
                    listOf(
                        AppBarMenuItem(
                            icon = IconData.ResourceIcon(R.drawable.i_plus_octagon),
                            iconConfig = IconConfig(size = 18),
                            text = "Добавить устройство",
                        ),
                        AppBarMenuItem(
                            icon = IconData.ResourceIcon(R.drawable.i_trash),
                            iconConfig = IconConfig(size = 18, color = Color.Red),
                            text = "Удалить списки",
                        ),
                    ),
            )
        },
    ) { innerPadding ->
        CompareScreenContent(viewModel, innerPadding)
    }
}
