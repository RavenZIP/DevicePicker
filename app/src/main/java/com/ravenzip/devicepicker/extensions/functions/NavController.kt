package com.ravenzip.devicepicker.extensions.functions

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

// TODO разобраться с навигацией (в том числе с навигацией нижнего меню)

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

/** Навигация с поиском маршрутка в заднем стеке (и переходом к нему при наличии) */
fun NavHostController.navigateWithSearchRouteInBackStack(route: String) {
    val isInBackStack =
        try {
            this.getBackStackEntry(route)
            true
        } catch (e: IllegalArgumentException) {
            false
        }

    if (isInBackStack) {
        this.popBackStack(route, inclusive = false)
    } else {
        this.navigate(route)
    }
}
