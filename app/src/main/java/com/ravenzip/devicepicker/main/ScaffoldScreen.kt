package com.ravenzip.devicepicker.main

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ravenzip.devicepicker.navigation.BottomBarGraph
import com.ravenzip.devicepicker.navigation.HomeScreenNavGraph
import com.ravenzip.workshop.components.BottomAppBar
import com.ravenzip.workshop.data.BottomNavigationItem

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScaffoldScreen(navController: NavHostController = rememberNavController()) {
    Scaffold(
        topBar = {},
        bottomBar = {
            BottomAppBar(
                navController = navController,
                buttonsList = buttonsList)
        }
    ) {
        HomeScreenNavGraph(navController = navController)
    }
}

private val buttonsList = listOf(
    BottomNavigationItem(
        label = "Главная",
        route = BottomBarGraph.HOME,
        icon = Icons.Outlined.Home,
        hasNews = false
    ),
    BottomNavigationItem(
        label = "Поиск",
        route = BottomBarGraph.SEARCH,
        icon = Icons.Outlined.Search,
        hasNews = false
    ),
    BottomNavigationItem(
        label = "Избранное",
        route = BottomBarGraph.FAVOURITES,
        icon = Icons.Outlined.FavoriteBorder,
        hasNews = false
    ),
    BottomNavigationItem(
        label = "Сравнение",
        route = BottomBarGraph.COMPARE,
        icon = Icons.Outlined.Refresh,
        hasNews = false
    ),
    BottomNavigationItem(
        label = "Профиль",
        route = BottomBarGraph.USER_PROFILE,
        icon = Icons.Outlined.AccountCircle,
        hasNews = false
    ),
)
