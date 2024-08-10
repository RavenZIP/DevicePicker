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
import com.ravenzip.workshop.data.AppBarItem
import com.ravenzip.workshop.data.BottomNavigationItem
import com.ravenzip.workshop.data.IconConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ScaffoldScreen(
    navController: NavHostController = rememberNavController(),
    userDataByViewModel: StateFlow<User>,
    getUser: () -> FirebaseUser?,
    getUserData: suspend (user: FirebaseUser?) -> Flow<User>,
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
        changeBottomBarState = { bottomBarState.value = it })

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
            getUser = getUser,
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
            icon = IconConfig(value = ImageVector.vectorResource(R.drawable.i_home), size = 20),
            hasNews = false,
        )

    val searchButton =
        BottomNavigationItem(
            label = "Поиск",
            route = BottomBarGraph.SEARCH,
            icon = IconConfig(value = ImageVector.vectorResource(R.drawable.i_search), size = 20),
            hasNews = false,
        )

    val favouriteButton =
        BottomNavigationItem(
            label = "Избранное",
            route = BottomBarGraph.FAVOURITES,
            icon = IconConfig(value = ImageVector.vectorResource(R.drawable.i_heart), size = 20),
            hasNews = false,
        )

    val compareButton =
        BottomNavigationItem(
            label = "Сравнение",
            route = BottomBarGraph.COMPARE,
            icon = IconConfig(value = ImageVector.vectorResource(R.drawable.i_compare), size = 20),
            hasNews = false,
        )

    val userProfileButton =
        BottomNavigationItem(
            label = "Профиль",
            route = BottomBarGraph.USER_PROFILE,
            icon = IconConfig(value = ImageVector.vectorResource(R.drawable.i_user), size = 20),
            hasNews = false,
        )

    return listOf(homeButton, searchButton, favouriteButton, compareButton, userProfileButton)
}

@Composable
private fun deviceInfoScreenTopAppBarItemList(): List<AppBarItem> {
    val favouriteIcon =
        IconConfig(value = ImageVector.vectorResource(R.drawable.i_heart), size = 20)
    val compareIcon =
        IconConfig(value = ImageVector.vectorResource(R.drawable.i_compare), size = 20)

    val favouriteButton = remember {
        mutableStateOf(AppBarItem(icon = favouriteIcon, onClick = {}))
    }
    val compareButton = remember { mutableStateOf(AppBarItem(icon = compareIcon, onClick = {})) }

    return listOf(favouriteButton.value, compareButton.value)
}

@Composable
private fun ChangeScaffoldItemsState(
    navController: NavController,
    currentRoute: String?,
    setTopAppBarState: (topAppBarState: TopAppBarState) -> Unit,
    setSearchBarState: (searchBarState: SearchBarState) -> Unit,
    setTopAppBarType: (topAppBarType: TopAppBarTypeEnum) -> Unit,
    changeBottomBarState: (isVisible: Boolean) -> Unit
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
                    menuItems = deviceInfoScreenTopAppBarItemList())

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
            setTopAppBarType(TopAppBarTypeEnum.TopAppBarWithMenu)
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
