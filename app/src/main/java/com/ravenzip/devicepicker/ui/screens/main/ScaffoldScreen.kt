package com.ravenzip.devicepicker.ui.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseUser
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.constants.enums.TopAppBarTypeEnum
import com.ravenzip.devicepicker.model.User
import com.ravenzip.devicepicker.navigation.graphs.MainNavigationGraph
import com.ravenzip.devicepicker.navigation.models.BottomBarGraph
import com.ravenzip.devicepicker.navigation.models.HomeGraph
import com.ravenzip.devicepicker.navigation.models.UserProfileGraph
import com.ravenzip.devicepicker.state.SearchBarState
import com.ravenzip.devicepicker.state.TopAppBarState
import com.ravenzip.devicepicker.viewmodels.TopAppBarViewModel
import com.ravenzip.workshop.components.BottomNavigationBar
import com.ravenzip.workshop.components.SearchBar
import com.ravenzip.workshop.components.TopAppBar
import com.ravenzip.workshop.components.TopAppBarWithMenu
import com.ravenzip.workshop.data.appbar.AppBarItem
import com.ravenzip.workshop.data.appbar.BottomNavigationItem
import com.ravenzip.workshop.data.icon.IconConfig
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ScaffoldScreen(
    navController: NavHostController = rememberNavController(),
    userDataByViewModel: StateFlow<User>,
    firebaseUser: FirebaseUser?,
    getUserData: suspend () -> Unit,
    logout: suspend () -> Unit,
) {
    val topAppBarViewModel = hiltViewModel<TopAppBarViewModel>()
    val topAppBarState = topAppBarViewModel.topAppBarState.collectAsState().value
    val searchBarState = topAppBarViewModel.searchBarState.collectAsState().value
    val topAppBarType = topAppBarViewModel.type.collectAsState().value
    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    ChangeScaffoldItemsState(
        navController = navController,
        currentRoute = navBackStackEntry?.destination?.route,
        setTopAppBarState = { state -> topAppBarViewModel.setTopAppBarState(state) },
        setSearchBarState = { state -> topAppBarViewModel.setSearchBarState(state) },
        setTopAppBarType = { type -> topAppBarViewModel.setType(type) },
        changeBottomBarState = { bottomBarState.value = it },
    )

    Scaffold(
        topBar = {
            TopAppBar(
                type = topAppBarType,
                topAppBarState = topAppBarState,
                searchBarState = searchBarState,
            )
        },
        bottomBar = {
            AnimatedVisibility(visible = bottomBarState.value, enter = fadeIn(), exit = fadeOut()) {
                BottomNavigationBar(
                    navController = navController,
                    buttonsList = generateMenuItems(),
                )
            }
        },
    ) { padding ->
        MainNavigationGraph(
            navController = navController,
            padding = padding,
            userDataByViewModel = userDataByViewModel,
            firebaseUser = firebaseUser,
            getUserData = getUserData,
            logout = logout,
        )
    }
}

@Composable
private fun TopAppBar(
    type: TopAppBarTypeEnum,
    topAppBarState: TopAppBarState,
    searchBarState: SearchBarState,
) {
    when (type) {
        TopAppBarTypeEnum.TopAppBar ->
            TopAppBar(
                title = topAppBarState.text,
                backArrow = topAppBarState.backArrow,
                items = topAppBarState.menuItems,
            )

        TopAppBarTypeEnum.TopAppBarWithMenu -> {
            TopAppBarWithMenu(title = topAppBarState.text)
        }

        TopAppBarTypeEnum.SearchBar -> {
            val query = remember { mutableStateOf("") }
            SearchBar(
                query = query,
                placeholder = searchBarState.placeholder,
                onSearch = { searchBarState.onSearch() },
            )
        }
    }
}

@Composable
private fun generateMenuItems(): List<BottomNavigationItem> {
    val homeButton =
        BottomNavigationItem(
            label = "Главная",
            route = BottomBarGraph.HOME,
            icon = ImageVector.vectorResource(R.drawable.i_home),
            iconConfig = IconConfig.Small,
            hasNews = false,
        )

    val searchButton =
        BottomNavigationItem(
            label = "Поиск",
            route = BottomBarGraph.SEARCH,
            icon = ImageVector.vectorResource(R.drawable.i_search),
            iconConfig = IconConfig.Small,
            hasNews = false,
        )

    val favouriteButton =
        BottomNavigationItem(
            label = "Избранное",
            route = BottomBarGraph.FAVOURITES,
            icon = ImageVector.vectorResource(R.drawable.i_heart),
            iconConfig = IconConfig.Small,
            hasNews = false,
        )

    val compareButton =
        BottomNavigationItem(
            label = "Сравнение",
            route = BottomBarGraph.COMPARE,
            icon = ImageVector.vectorResource(R.drawable.i_compare),
            iconConfig = IconConfig.Small,
            hasNews = false,
        )

    val userProfileButton =
        BottomNavigationItem(
            label = "Профиль",
            route = BottomBarGraph.USER_PROFILE,
            icon = ImageVector.vectorResource(R.drawable.i_user),
            iconConfig = IconConfig.Small,
            hasNews = false,
        )

    return listOf(homeButton, searchButton, favouriteButton, compareButton, userProfileButton)
}

@Composable
private fun deviceInfoScreenTopAppBarItemList(): List<AppBarItem> {
    val favouriteButton =
        AppBarItem(
            icon = ImageVector.vectorResource(R.drawable.i_heart),
            iconConfig = IconConfig.Small,
            onClick = {},
        )

    val compareButton =
        AppBarItem(
            icon = ImageVector.vectorResource(R.drawable.i_compare),
            iconConfig = IconConfig.Small,
            onClick = {},
        )

    return remember { listOf(favouriteButton, compareButton) }
}

@Composable
private fun ChangeScaffoldItemsState(
    navController: NavController,
    currentRoute: String?,
    setTopAppBarState: (topAppBarState: TopAppBarState) -> Unit,
    setSearchBarState: (searchBarState: SearchBarState) -> Unit,
    setTopAppBarType: (topAppBarType: TopAppBarTypeEnum) -> Unit,
    changeBottomBarState: (isVisible: Boolean) -> Unit,
) {
    when (currentRoute) {
        BottomBarGraph.HOME -> {
            setTopAppBarState(TopAppBarState.createTopAppBarState("Главная"))
            setTopAppBarType(TopAppBarTypeEnum.TopAppBar)
            changeBottomBarState(true)
        }

        HomeGraph.DEVICE_INFO -> {
            val topAppBarState =
                TopAppBarState.createTopAppBarState(
                    onClickToBackArrow = { navController.navigateUp() },
                    menuItems = deviceInfoScreenTopAppBarItemList(),
                )

            setTopAppBarState(topAppBarState)
            setTopAppBarType(TopAppBarTypeEnum.TopAppBar)

            changeBottomBarState(false)
        }

        BottomBarGraph.SEARCH -> {
            setSearchBarState(SearchBarState.createSearchBarState())
            setTopAppBarType(TopAppBarTypeEnum.SearchBar)
        }

        BottomBarGraph.FAVOURITES -> {
            setTopAppBarState(TopAppBarState.createTopAppBarState("Избранное"))
            setTopAppBarType(TopAppBarTypeEnum.TopAppBar)
        }

        BottomBarGraph.COMPARE -> {
            setTopAppBarState(TopAppBarState.createTopAppBarState("Сравнение"))
            setTopAppBarType(TopAppBarTypeEnum.TopAppBar)
        }

        BottomBarGraph.USER_PROFILE -> {
            setTopAppBarState(TopAppBarState.createTopAppBarState("Профиль"))
            setTopAppBarType(TopAppBarTypeEnum.TopAppBar)
            changeBottomBarState(true)
        }

        UserProfileGraph.ADMIN_PANEL -> {
            setTopAppBarState(TopAppBarState.createTopAppBarState("Панель администратора"))
            changeBottomBarState(false)
        }

        else -> {
            // do nothing
        }
    }
}
