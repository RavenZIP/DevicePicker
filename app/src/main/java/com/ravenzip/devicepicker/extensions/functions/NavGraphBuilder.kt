package com.ravenzip.devicepicker.extensions.functions

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

/** Добавление [Composable] в [NavGraphBuilder] с fadeIn и fadeOut анимацией */
fun NavGraphBuilder.composable(
    route: String,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    this.composable(
        route = route,
        enterTransition = { fadeIn(animationSpec = tween(300, easing = LinearEasing)) },
        exitTransition = { fadeOut(animationSpec = tween(300, easing = LinearEasing)) },
        content = content)
}
