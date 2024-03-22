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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.ravenzip.devicepicker.navigation.graphs.RootNavigationGraph
import com.ravenzip.devicepicker.services.DataService
import com.ravenzip.devicepicker.services.ImageService
import com.ravenzip.devicepicker.services.InitializeSnackBarIcons
import com.ravenzip.devicepicker.services.LowPriceDevicesService
import com.ravenzip.devicepicker.services.PopularDevicesService
import com.ravenzip.devicepicker.services.SplashScreenService
import com.ravenzip.devicepicker.services.getUser
import com.ravenzip.devicepicker.ui.theme.DevicePickerTheme
import kotlinx.coroutines.flow.flowOf

class MainActivity : ComponentActivity() {
    private val dataService: DataService by viewModels()
    private val splashScreenService: SplashScreenService by viewModels()
    private val popularDevicesService: PopularDevicesService by viewModels()
    private val lowPriceDevicesService: LowPriceDevicesService by viewModels()
    private val imageService: ImageService by viewModels()

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
                        val isLoadingDeviceInfo = remember { mutableStateOf(true) }
                        val isLoadingDeviceImages = remember { mutableStateOf(false) }
                        val popularDevices = popularDevicesService.devices.collectAsState().value

                        LaunchedEffect(
                            key1 = isLoadingDeviceInfo.value,
                            key2 = isLoadingDeviceImages.value
                        ) {
                            // Сначала грузим все что из Realtime Database
                            if (isLoadingDeviceInfo.value) {
                                flowOf(
                                        popularDevicesService.get(),
                                        lowPriceDevicesService.get(),
                                        //
                                        // dataService.getSimilarDevices(),
                                        //
                                        // dataService.getCompanyBestDevices(),
                                        //
                                        // dataService.getUnknown()
                                    )
                                    .collect {
                                        isLoadingDeviceInfo.value = false
                                        isLoadingDeviceImages.value = true
                                    }
                            }

                            // Потом на осное полученных данных грузим из Storage картинки
                            if (isLoadingDeviceImages.value) {
                                // Доработать
                                flowOf(popularDevices.forEach { imageService.getImage() }).collect {
                                    isLoadingDeviceImages.value = false
                                }
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
