package com.ravenzip.devicepicker.common.utils.extension

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun ButtonDefaults.inverseColors(): ButtonColors =
    this.buttonColors(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary)

/** Mix - потому что surfaceContainer похож на смесь primary и surface */
@Composable
fun ButtonDefaults.inverseMixColors() =
    this.buttonColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.primary)

@Composable
fun ButtonDefaults.containerColor() =
    this.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)

@Composable
fun ButtonDefaults.veryLightPrimary(contentColor: Color = LocalContentColor.current) =
    this.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary.copy(0.08f), contentColor = contentColor)
