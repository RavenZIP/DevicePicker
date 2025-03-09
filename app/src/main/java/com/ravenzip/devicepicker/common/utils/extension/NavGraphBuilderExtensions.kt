package com.ravenzip.devicepicker.common.utils.extension

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
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
fun NavGraphBuilder.navigateWithSlideAnimation(
    route: String,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    this.composable(
        route = route,
        enterTransition = {
            fadeIn(animationSpec = tween(300, easing = LinearEasing)) +
                slideInHorizontally(animationSpec = tween(300, easing = LinearEasing)) { it / 2 }
        },
        exitTransition = {
            fadeOut(animationSpec = tween(300, easing = LinearEasing)) +
                slideOutHorizontally(animationSpec = tween(300, easing = LinearEasing))
        },
        content = content,
    )
}
