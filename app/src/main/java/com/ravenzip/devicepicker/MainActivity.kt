package com.ravenzip.devicepicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.ravenzip.devicepicker.navigation.graphs.RootNavigationGraph
import com.ravenzip.devicepicker.services.DataService
import com.ravenzip.devicepicker.services.InitializeSnackBarIcons
import com.ravenzip.devicepicker.services.SplashScreenService
import com.ravenzip.devicepicker.services.getUser
import com.ravenzip.devicepicker.ui.theme.DevicePickerTheme

class MainActivity : ComponentActivity() {
    private val dataService: DataService by viewModels()
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

                    // Если пользователь авторизован,
                    // то параллельно грузим каждую категорию
                    // TODO: переписать с использованием сторонней библиотеки распараллеливания
                    if (getUser() !== null) {
                        val isLoading = remember {
                            mutableStateListOf(true, true, true, true, true)
                        }

                        LaunchedEffect(keys = isLoading.toList().toBooleanArray().toTypedArray()) {
                            if (isLoading[0]) {
                                dataService.getPopularThisWeek()
                                isLoading[0] = false
                            }

                            if (isLoading[1]) {
                                dataService.getSimilarDevices()
                                isLoading[1] = false
                            }

                            if (isLoading[2]) {
                                dataService.getCompanyBestDevices()
                                isLoading[2] = false
                            }

                            if (isLoading[3]) {
                                dataService.getLowPrice()
                                isLoading[3] = false
                            }

                            if (isLoading[4]) {
                                dataService.getUnknown()
                                isLoading[4] = false
                            }
                        }
                    }

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
