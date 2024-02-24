package com.ravenzip.devicepicker.services

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.ui.theme.errorColor
import com.ravenzip.devicepicker.ui.theme.successColor
import com.ravenzip.devicepicker.ui.theme.warningColor
import com.ravenzip.workshop.data.IconParameters
import com.ravenzip.workshop.data.SnackBarVisualsExtended

private lateinit var default: ImageVector
private lateinit var success: ImageVector
private lateinit var warning: ImageVector
private lateinit var error: ImageVector

@Composable
fun InitializeSnackBarIcons() {
    default = ImageVector.vectorResource(R.drawable.i_notification)
    success = ImageVector.vectorResource(R.drawable.i_success)
    warning = ImageVector.vectorResource(R.drawable.i_warning)
    error = ImageVector.vectorResource(R.drawable.i_error)
}

suspend fun SnackbarHostState.showMessage(message: String) {
    this.showSnackbar(
        SnackBarVisualsExtended(message = message, icon = IconParameters(value = default))
    )
}

suspend fun SnackbarHostState.showSuccess(message: String) {
    this.showSnackbar(
        SnackBarVisualsExtended(
            message = message,
            icon = IconParameters(value = success, color = successColor)
        )
    )
}

suspend fun SnackbarHostState.showWarning(message: String) {
    this.showSnackbar(
        SnackBarVisualsExtended(
            message = message,
            icon = IconParameters(value = warning, color = warningColor)
        )
    )
}

suspend fun SnackbarHostState.showError(message: String) {
    this.showSnackbar(
        SnackBarVisualsExtended(
            message = message,
            icon = IconParameters(value = error, color = errorColor)
        )
    )
}
