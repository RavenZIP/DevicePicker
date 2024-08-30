package com.ravenzip.devicepicker.extensions.functions

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

/** Навигация с очисткой заднего стека навигаци */
fun NavHostController.navigateWithoutPreviousRoute(
    startDestination: String? = null,
    targetDestination: String,
) {
    this.navigate(targetDestination) {
        val calculatedStartDestination =
            this@navigateWithoutPreviousRoute.graph.findStartDestination().route!!
        popUpTo(startDestination ?: calculatedStartDestination) { inclusive = true }
    }
}
