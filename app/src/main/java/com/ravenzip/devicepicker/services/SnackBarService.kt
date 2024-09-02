package com.ravenzip.devicepicker.services

import androidx.compose.material3.SnackbarHostState
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.ui.theme.errorColor
import com.ravenzip.devicepicker.ui.theme.successColor
import com.ravenzip.devicepicker.ui.theme.warningColor
import com.ravenzip.workshop.data.SnackBarVisualsConfig
import com.ravenzip.workshop.data.icon.Icon
import com.ravenzip.workshop.data.icon.IconConfig

suspend fun SnackbarHostState.showMessage(message: String) {
    this.showSnackbar(
        SnackBarVisualsConfig(
            message = message,
            icon = Icon.ResourceIcon(R.drawable.i_notification),
            iconConfig = IconConfig.Default,
        )
    )
}

suspend fun SnackbarHostState.showSuccess(message: String) {
    this.showSnackbar(
        SnackBarVisualsConfig(
            message = message,
            icon = Icon.ResourceIcon(R.drawable.i_success),
            iconConfig = IconConfig(color = successColor),
        )
    )
}

suspend fun SnackbarHostState.showWarning(message: String) {
    this.showSnackbar(
        SnackBarVisualsConfig(
            message = message,
            icon = Icon.ResourceIcon(R.drawable.i_warning),
            iconConfig = IconConfig(color = warningColor),
        )
    )
}

suspend fun SnackbarHostState.showError(message: String) {
    this.showSnackbar(
        SnackBarVisualsConfig(
            message = message,
            icon = Icon.ResourceIcon(R.drawable.i_error),
            iconConfig = IconConfig(color = errorColor),
        )
    )
}
