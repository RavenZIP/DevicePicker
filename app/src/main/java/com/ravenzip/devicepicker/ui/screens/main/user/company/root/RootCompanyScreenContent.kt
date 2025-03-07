package com.ravenzip.devicepicker.ui.screens.main.user.company.root

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.extensions.functions.veryLightPrimary
import com.ravenzip.devicepicker.ui.screens.main.user.company.enum.CompanyScreenActionsEnum
import com.ravenzip.devicepicker.ui.screens.main.user.company.viewmodel.CompanyRootViewModel
import com.ravenzip.workshop.components.InfoCard
import com.ravenzip.workshop.components.RadioGroup
import com.ravenzip.workshop.data.icon.IconData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyRootScreenContent(viewModel: CompanyRootViewModel) {
    RadioGroup(
        state = viewModel.companyScreenTypeState,
        source = CompanyScreenActionsEnum.entries,
        view = { it.description },
        comparableKey = { it },
    )

    Spacer(modifier = Modifier.height(5.dp))

    InfoCard(
        icon = IconData.ResourceIcon(R.drawable.i_info),
        title = "Важно",
        text =
            "Перед началом работы с разделом \"Компания\" необходимо создать свою компанию, либо вступить в уже существующую.",
        colors = CardDefaults.veryLightPrimary(),
    )
}
