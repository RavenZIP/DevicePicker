package com.ravenzip.devicepicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.ravenzip.devicepicker.navigation.graphs.RootNavigationGraph
import com.ravenzip.devicepicker.common.theme.DevicePickerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Скрываем ActionBar, который появляется после отображения SplashScreen
        // Проблема актуальна для эмуляторов, на реальном устройстве не отображается
        // Скорее всего связано с тем, что не срабатывает postSplashScreenTheme
        actionBar?.hide()

        setContent {
            DevicePickerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    installSplashScreen()

                    RootNavigationGraph(navController = rememberNavController())
                }
            }
        }
    }
}
