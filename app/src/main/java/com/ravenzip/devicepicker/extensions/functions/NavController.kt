package com.ravenzip.devicepicker.extensions.functions

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

/** Навигация с очисткой заднего стека навигаци */
fun NavHostController.navigateWithoutPreviousRoute(destination: String) {
    this.navigate(destination) {
        val startDestination = this@navigateWithoutPreviousRoute.graph.findStartDestination().id
        popUpTo(startDestination) { inclusive = true }
    }
}
