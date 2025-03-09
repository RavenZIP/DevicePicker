package com.ravenzip.devicepicker.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/** Полностью центрированный горизонтальный контейнер */
@Composable
fun CenterRow(modifier: Modifier = Modifier, content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        content()
    }
}

/** Горизонтальный контейнер с центрированием по вертикали */
@Composable
fun VerticalCenterRow(modifier: Modifier = Modifier, content: @Composable RowScope.() -> Unit) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) { content() }
}

/** Горизонтальный контейнер с центрированием по горизонтали */
@Composable
fun HorizontalCenterRow(modifier: Modifier = Modifier, content: @Composable RowScope.() -> Unit) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.Center) { content() }
}
