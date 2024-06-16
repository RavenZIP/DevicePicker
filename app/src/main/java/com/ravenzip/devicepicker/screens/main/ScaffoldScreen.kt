package com.ravenzip.devicepicker.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.enums.TopAppBarStateEnum
import com.ravenzip.devicepicker.navigation.graphs.HomeScreenNavGraph
import com.ravenzip.devicepicker.navigation.models.BottomBarGraph
import com.ravenzip.devicepicker.viewmodels.TopAppBarViewModel
import com.ravenzip.devicepicker.viewmodels.UserViewModel
import com.ravenzip.workshop.components.BottomNavigationBar
import com.ravenzip.workshop.components.SearchBar
import com.ravenzip.workshop.components.TopAppBar
import com.ravenzip.workshop.data.BottomNavigationItem
import com.ravenzip.workshop.data.IconParameters

@Composable
fun ScaffoldScreen(
    navController: NavHostController = rememberNavController(),
    userViewModel: UserViewModel
) {
    val topAppBarViewModel = hiltViewModel<TopAppBarViewModel>()
    val text = topAppBarViewModel.text.collectAsState().value
    val state = topAppBarViewModel.state.collectAsState().value
    val onSearch = topAppBarViewModel.onSearch.collectAsState().value
    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

    Scaffold(
        topBar = { TopAppBar(state = state, text = text, onSearch = onSearch) },
        bottomBar = {
            AnimatedVisibility(visible = bottomBarState.value, enter = fadeIn(), exit = fadeOut()) {
                BottomNavigationBar(
                    navController = navController,
                    buttonsList = generateMenuItems()
                )
            }
        }
    ) {
        HomeScreenNavGraph(
            navController = navController,
            padding = it,
            topAppBarViewModel = topAppBarViewModel,
            userViewModel = userViewModel,
            bottomBarState = bottomBarState
        )
    }
}

@Composable
private fun TopAppBar(state: TopAppBarStateEnum, text: String, onSearch: () -> Unit) {
    when (state) {
        TopAppBarStateEnum.TopAppBar -> TopAppBar(title = text)
        TopAppBarStateEnum.TopAppBarWithMenu -> {}
        TopAppBarStateEnum.SearchBar -> {
            val query = remember { mutableStateOf("") }
            SearchBar(query = query, placeholder = text, onSearch = { onSearch() })
        }
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
