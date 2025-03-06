package com.ravenzip.devicepicker.ui.screens.main.user.company.devices

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.ui.screens.main.user.company.EmptyScreenCardWithAction
import com.ravenzip.devicepicker.ui.screens.main.user.company.viewmodel.CompanyInfoViewModel
import com.ravenzip.devicepicker.ui.theme.warningColor

@Composable
fun DevicesCompanyScreenContent(viewModel: CompanyInfoViewModel) {
    val devices = viewModel.devices.collectAsStateWithLifecycle().value

    if (devices.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            EmptyScreenCardWithAction(
                text = "Список устройств пуст",
                description =
                    "На данный момент вы не добавили ни одно устройство в список. Вы можете сделать это нажав по кнопке \"Добавить устройство\"",
                icon = ImageVector.vectorResource(id = R.drawable.i_warning),
                iconColor = warningColor,
                buttonText = "Добавить устройство",
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(devices) { Text("Устройство") }
        }
    }
}
