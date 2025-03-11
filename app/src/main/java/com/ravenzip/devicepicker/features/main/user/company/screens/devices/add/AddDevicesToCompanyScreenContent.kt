package com.ravenzip.devicepicker.features.main.user.company.screens.devices.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravenzip.devicepicker.common.components.AlertDialogWithField
import com.ravenzip.devicepicker.common.components.RowDeviceCard
import com.ravenzip.devicepicker.navigation.models.HomeGraph
import com.ravenzip.workshop.components.SinglenessOutlinedTextField
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDevicesToCompanyScreenContent(viewModel: AddDevicesToCompanyViewModel) {
    val devices = viewModel.devices.collectAsStateWithLifecycle().value
    val dialogWindowIsShowed =
        viewModel.alertDialog.isShowed.collectAsStateWithLifecycle(false).value

    val composableScope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        items(devices) { device ->
            RowDeviceCard(
                device = device,
                isFavourite = true,
                onFavouriteClick = {},
                onCompareClick = {},
                onCardClick = {
                    composableScope.launch {
                        viewModel.navigateTo.emit("${HomeGraph.DEVICE_INFO}/${device.uid}")
                    }
                },
                onAddToCompanyClick = { viewModel.alertDialog.show() },
            )
        }
    }

    if (dialogWindowIsShowed) {
        AlertDialogWithField(
            title = "Добавление устройства в компанию",
            text = "Укажите число устройств, которое хотите добавить",
            textField = {
                SinglenessOutlinedTextField(
                    state = viewModel.deviceCounterState,
                    width = 1f,
                    label = "Количество устройств",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
            },
            onDismissText = "Отменить",
            onDismiss = { viewModel.alertDialog.dismiss() },
            onConfirmationText = "Подтвердить",
            onConfirmation = { viewModel.alertDialog.confirm() },
        )
    }
}
