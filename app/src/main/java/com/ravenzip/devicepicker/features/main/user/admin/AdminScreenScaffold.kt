package com.ravenzip.devicepicker.features.main.user.admin

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.ravenzip.devicepicker.R
import com.ravenzip.workshop.components.TopAppBar
import com.ravenzip.workshop.data.appbar.BackArrow
import com.ravenzip.workshop.data.icon.IconConfig
import com.ravenzip.workshop.data.icon.IconData

@Composable
fun AdminScreenScaffold(padding: PaddingValues, navigateToUserProfile: () -> Unit) {
    val backArrow = remember {
        BackArrow(
            icon = IconData.ResourceIcon(R.drawable.i_back),
            iconConfig = IconConfig.Default,
            onClick = navigateToUserProfile,
        )
    }

    Scaffold(
        modifier = Modifier.padding(padding),
        topBar = { TopAppBar("Администрирование", backArrow = backArrow) },
    ) { innerPadding ->
        AdminScreenContent(innerPadding)
    }
}
