package com.ravenzip.devicepicker.extensions.functions

import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun CardDefaults.defaultCardColors() =
    this.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = LocalContentColor.current,
    )

@Composable
fun CardDefaults.veryLightPrimary(alpha: Float = 0.05f) =
    this.cardColors(
        containerColor = MaterialTheme.colorScheme.primary.copy(alpha),
        contentColor = LocalContentColor.current,
    )
