package com.ravenzip.devicepicker.features.main.user.company.screens.devices

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.common.EmptyScreenCardWithAction
import com.ravenzip.devicepicker.common.theme.warningColor
import com.ravenzip.devicepicker.features.main.user.company.screens.info.CompanyInfoViewModel
import com.ravenzip.devicepicker.navigation.models.CompanyGraph
import kotlinx.coroutines.launch

@Composable
fun DevicesCompanyScreenContent(
    companyInfoViewModel: CompanyInfoViewModel,
    viewModel: CompanyDevicesViewModel,
) {
    val devices = companyInfoViewModel.devices.collectAsStateWithLifecycle().value
    val devicesIsEmpty = companyInfoViewModel.devicesIsEmpty.collectAsStateWithLifecycle().value
    val composableScope = rememberCoroutineScope()

    if (devicesIsEmpty) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            EmptyScreenCardWithAction(
                text = "Список устройств пуст",
                description =
                    "На данный момент вы не добавили ни одно устройство в список. Вы можете сделать это нажав по кнопке \"Добавить устройство\"",
                icon = ImageVector.vectorResource(id = R.drawable.i_warning),
                iconColor = warningColor,
                buttonText = "Добавить устройство",
            ) {
                composableScope.launch { viewModel.navigateTo.emit(CompanyGraph.ADD_DEVICES) }
            }
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
