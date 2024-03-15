package com.ravenzip.devicepicker.extensions.functions

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

// TODO переписать на fun ButtonDefaults.buttonColors

@Composable
fun getInverseColors(): ButtonColors {
    return ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary
    )
}

/** Mix - потому что surfaceContainer похож на смесь primary и surface */
@Composable
fun getInverseMixColors(): ButtonColors {
    return ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun getContainerColor(): ButtonColors {
    return ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
}
