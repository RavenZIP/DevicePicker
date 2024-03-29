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
import androidx.navigation.compose.rememberNavController
import com.ravenzip.devicepicker.navigation.graphs.RootNavigationGraph
import com.ravenzip.devicepicker.services.InitializeSnackBarIcons
import com.ravenzip.devicepicker.services.SplashScreenService
import com.ravenzip.devicepicker.ui.theme.DevicePickerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val splashScreenService: SplashScreenService by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DevicePickerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    installSplashScreen().setKeepOnScreenCondition {
                        splashScreenService.isLoading.value
                    }
                    InitializeSnackBarIcons()

                    val startDestination =
                        splashScreenService.startDestination.collectAsState().value

                    RootNavigationGraph(
                        navController = rememberNavController(),
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}
