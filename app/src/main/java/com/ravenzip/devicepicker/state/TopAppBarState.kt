package com.ravenzip.devicepicker.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.ravenzip.devicepicker.R
import com.ravenzip.workshop.data.AppBarItem
import com.ravenzip.workshop.data.BackArrow
import com.ravenzip.workshop.data.IconParameters

data class TopAppBarState(
    val text: String,
    val backArrow: BackArrow?,
    val menuItems: List<AppBarItem>
) {
    constructor() : this(text = "", backArrow = null, menuItems = listOf())

    companion object {
        /** Кастомный конструктор для модели состояния TopAppBar (задается только текст) */
        fun createTopAppBarState(text: String = ""): TopAppBarState {
            return TopAppBarState(text = text, backArrow = null, menuItems = listOf())
        }

        /** Кастомный конструктор для модели состояния TopAppBar с кнопкой назад */
        @Composable
        fun createTopAppBarState(
            text: String = "",
            backArrowOnClick: () -> Unit = {},
            menuItems: List<AppBarItem> = listOf()
        ): TopAppBarState {
            val backArrowIcon =
                IconParameters(value = ImageVector.vectorResource(R.drawable.i_back))
            val backArrow = remember {
                mutableStateOf(BackArrow(icon = backArrowIcon, onClick = { backArrowOnClick() }))
            }

            return TopAppBarState(text = text, backArrow = backArrow.value, menuItems = menuItems)
        }
    }
}
