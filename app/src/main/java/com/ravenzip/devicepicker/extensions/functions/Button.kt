package com.ravenzip.devicepicker.extensions.functions

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

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
fun ButtonDefaults.surfaceVariant() =
    this.buttonColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = LocalContentColor.current)
