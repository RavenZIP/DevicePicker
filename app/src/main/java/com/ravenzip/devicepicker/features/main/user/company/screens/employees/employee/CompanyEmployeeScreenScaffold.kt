package com.ravenzip.devicepicker.features.main.user.company.screens.employees.employee

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.features.main.user.company.screens.info.CompanyInfoViewModel
import com.ravenzip.workshop.components.TopAppBarWithMenu
import com.ravenzip.workshop.data.appbar.AppBarMenuItem
import com.ravenzip.workshop.data.appbar.BackArrow
import com.ravenzip.workshop.data.icon.IconConfig
import com.ravenzip.workshop.data.icon.IconData

@Composable
fun EmployeeCompanyScreenScaffold(
    viewModel: CompanyInfoViewModel,
    padding: PaddingValues,
    navigateBack: () -> Unit,
) {
    val backArrow = remember {
        BackArrow(
            icon = IconData.ResourceIcon(R.drawable.i_back),
            iconConfig = IconConfig.Default,
            onClick = navigateBack,
        )
    }

    Scaffold(
        modifier = Modifier.padding(padding),
        topBar = {
            TopAppBarWithMenu(
                "Сотрудник",
                backArrow = backArrow,
                items =
                    listOf(
                        AppBarMenuItem(
                            icon = IconData.ResourceIcon(R.drawable.i_plus_octagon),
                            iconConfig = IconConfig(size = 18),
                            text = "Привязать новое устройство",
                        ),
                        AppBarMenuItem(
                            icon = IconData.ResourceIcon(R.drawable.i_minus),
                            iconConfig = IconConfig(size = 18),
                            text = "Отвязать устройство",
                        ),
                        AppBarMenuItem(
                            icon = IconData.ResourceIcon(R.drawable.i_leaderboard),
                            iconConfig = IconConfig(size = 18),
                            text = "Изменить должность",
                        ),
                        AppBarMenuItem(
                            icon = IconData.ResourceIcon(R.drawable.i_delete_user),
                            iconConfig = IconConfig(size = 18, color = Color.Red),
                            text = "Исключить из организации",
                        ),
                    ),
            )
        },
    ) { innerPadding ->
        EmployeeCompanyScreenContent(viewModel, innerPadding)
    }
}
