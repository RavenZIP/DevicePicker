package com.ravenzip.devicepicker.screens.main

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.navigation.BottomBarGraph
import com.ravenzip.devicepicker.navigation.HomeScreenNavGraph
import com.ravenzip.workshop.components.BottomNavigationBar
import com.ravenzip.workshop.data.BottomNavigationItem
import com.ravenzip.workshop.data.IconParameters

@Composable
fun ScaffoldScreen(navController: NavHostController = rememberNavController()) {
    Scaffold(
        topBar = {},
        bottomBar = {
            BottomNavigationBar(navController = navController, buttonsList = generateMenuItems())
        }
    ) {
        HomeScreenNavGraph(navController = navController, padding = it)
    }
}

@Composable
private fun generateMenuItems(): List<BottomNavigationItem> {
    val homeButton =
        BottomNavigationItem(
            label = "Главная",
            route = BottomBarGraph.HOME,
            icon = IconParameters(value = ImageVector.vectorResource(R.drawable.i_home), size = 20),
            hasNews = false
        )

    val searchButton =
        BottomNavigationItem(
            label = "Поиск",
            route = BottomBarGraph.SEARCH,
            icon =
                IconParameters(value = ImageVector.vectorResource(R.drawable.i_search), size = 20),
            hasNews = false
        )

    val favouriteButton =
        BottomNavigationItem(
            label = "Избранное",
            route = BottomBarGraph.FAVOURITES,
            icon =
                IconParameters(value = ImageVector.vectorResource(R.drawable.i_heart), size = 20),
            hasNews = false
        )

    val compareButton =
        BottomNavigationItem(
            label = "Сравнение",
            route = BottomBarGraph.COMPARE,
            icon =
                IconParameters(value = ImageVector.vectorResource(R.drawable.i_compare), size = 20),
            hasNews = false
        )

    val userProfileButton =
        BottomNavigationItem(
            label = "Профиль",
            route = BottomBarGraph.USER_PROFILE,
            icon = IconParameters(value = ImageVector.vectorResource(R.drawable.i_user), size = 20),
            hasNews = false
        )

    return listOf(homeButton, searchButton, favouriteButton, compareButton, userProfileButton)
}
