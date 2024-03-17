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
import com.ravenzip.devicepicker.services.DataService
import com.ravenzip.devicepicker.services.InitializeSnackBarIcons
import com.ravenzip.devicepicker.services.SplashScreenService
import com.ravenzip.devicepicker.ui.theme.DevicePickerTheme

class MainActivity : ComponentActivity() {
    private val dataService: DataService by viewModels()
    private lateinit var splashScreenService: SplashScreenService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DevicePickerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Чтобы не ругался на as T
                    // По сути ничего страшного именно в данной ситуации нет,
                    // но as T в общем-то не очень хорошая затея
                    @Suppress("Unchecked_cast")
                    splashScreenService =
                        viewModel<SplashScreenService>(
                            factory =
                                object : ViewModelProvider.Factory {
                                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                        return SplashScreenService(dataService) as T
                                    }
                                }
                        )

                    installSplashScreen().setKeepOnScreenCondition {
                        splashScreenService.isLoading.value
                    }
                    InitializeSnackBarIcons()

                    val startDestination =
                        splashScreenService.startDestination.collectAsState().value

                    RootNavigationGraph(
                        navController = rememberNavController(),
                        startDestination = startDestination,
                        dataService = dataService
                    )
                }
            }
        }
    }
}
