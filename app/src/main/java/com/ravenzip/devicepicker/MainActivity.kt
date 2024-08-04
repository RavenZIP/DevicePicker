package com.ravenzip.devicepicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.ravenzip.devicepicker.navigation.graphs.RootNavigationGraph
import com.ravenzip.devicepicker.services.InitializeSnackBarIcons
import com.ravenzip.devicepicker.ui.theme.DevicePickerTheme
import com.ravenzip.devicepicker.viewmodels.SplashScreenViewModel
import com.ravenzip.devicepicker.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Скрываем ActionBar, который появляется после отображения SplashScreen
        // Проблема актуальна для эмуляторов, на рельном устройстве не отображается
        // Скорее всего связано с тем, что не срабатывает postSplashScreenTheme
        actionBar?.hide()

        setContent {
            DevicePickerTheme {
                val splashScreenViewModel =
                    viewModel<SplashScreenViewModel>(
                        factory =
                            object : ViewModelProvider.Factory {
                                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                                    SplashScreenViewModel(userViewModel) as T
                            },
                    )

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    installSplashScreen().setKeepOnScreenCondition {
                        splashScreenViewModel.isLoading.value
                    }
                    InitializeSnackBarIcons()

                    val startDestination =
                        splashScreenViewModel.startDestination.collectAsState().value

                    RootNavigationGraph(
                        navController = rememberNavController(),
                        startDestination = startDestination,
                        userViewModel = userViewModel,
                    )
                }
            }
        }
    }
}
