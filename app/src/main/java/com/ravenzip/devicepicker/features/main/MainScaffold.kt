package com.ravenzip.devicepicker.features.main

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.navigation.graphs.MainNavigationGraph
import com.ravenzip.devicepicker.navigation.models.BottomBarGraph
import com.ravenzip.workshop.components.BottomNavigationBar
import com.ravenzip.workshop.data.appbar.BottomNavigationItem
import com.ravenzip.workshop.data.icon.IconConfig
import com.ravenzip.workshop.data.icon.IconData

@Composable
fun MainScaffold(
    navController: NavHostController = rememberNavController(),
    navigateToSplashScreen: () -> Unit,
) {
    val menuItems = remember { generateMenuItems() }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController, buttonsList = menuItems) }
    ) { innerPadding ->
        MainNavigationGraph(
            navController = navController,
            padding = innerPadding,
            navigateToSplashScreen = navigateToSplashScreen,
        )
    }
}

private fun generateMenuItems(): List<BottomNavigationItem> {
    val homeButton =
        BottomNavigationItem(
            label = "Главная",
            route = BottomBarGraph.HOME,
            icon = IconData.ResourceIcon(R.drawable.i_home),
            iconConfig = IconConfig.Small,
            hasNews = false,
        )

    val searchButton =
        BottomNavigationItem(
            label = "Поиск",
            route = BottomBarGraph.SEARCH,
            icon = IconData.ResourceIcon(R.drawable.i_search),
            iconConfig = IconConfig.Small,
            hasNews = false,
        )

    val favouriteButton =
        BottomNavigationItem(
            label = "Избранное",
            route = BottomBarGraph.FAVOURITES,
            icon = IconData.ResourceIcon(R.drawable.i_heart),
            iconConfig = IconConfig.Small,
            hasNews = false,
        )

    val compareButton =
        BottomNavigationItem(
            label = "Сравнение",
            route = BottomBarGraph.COMPARE,
            icon = IconData.ResourceIcon(R.drawable.i_compare),
            iconConfig = IconConfig.Small,
            hasNews = false,
        )

    val userProfileButton =
        BottomNavigationItem(
            label = "Профиль",
            route = BottomBarGraph.USER_PROFILE,
            icon = IconData.ResourceIcon(R.drawable.i_user),
            iconConfig = IconConfig.Small,
            hasNews = false,
        )

    return listOf(homeButton, searchButton, favouriteButton, compareButton, userProfileButton)
}
