package com.ravenzip.devicepicker.extensions.functions

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

/** Добавление навигации с fadeIn и fadeOut анимацией */
fun NavGraphBuilder.navigateWithFadeAnimation(
    route: String,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    this.composable(
        route = route,
        enterTransition = { fadeIn(animationSpec = tween(300, easing = LinearEasing)) },
        exitTransition = { fadeOut(animationSpec = tween(300, easing = LinearEasing)) },
        content = content,
    )
}

/** Добавление навигации с fadeIn + slideIn и fadeOut + slideOut анимацией */
fun NavGraphBuilder.navigateWithSlideInAnimation(
    route: String,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    this.composable(
        route = route,
        enterTransition = {
            fadeIn(animationSpec = tween(300, easing = LinearEasing)) +
                slideIn(initialOffset = { IntOffset(100, 0) })
        },
        exitTransition = {
            fadeOut(animationSpec = tween(300, easing = LinearEasing)) +
                slideOut { IntOffset(-100, 0) }
        },
        content = content,
    )
}
