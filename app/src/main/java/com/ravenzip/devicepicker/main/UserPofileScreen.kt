package com.ravenzip.devicepicker.main

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.services.logout
import com.ravenzip.devicepicker.ui.theme.errorColor
import com.ravenzip.workshop.components.CustomButton
import com.ravenzip.workshop.components.Spinner
import com.ravenzip.workshop.data.IconParameters
import com.ravenzip.workshop.data.TextParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun UserProfileScreen(padding: PaddingValues) {
    val isLoading = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val packageManager: PackageManager = context.packageManager
    val intent: Intent? = packageManager.getLaunchIntentForPackage(context.packageName)
    val componentName: ComponentName? = intent?.component
    val mainIntent: Intent = Intent.makeRestartActivityTask(componentName)

    Column(
        modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Профиль",
            modifier = Modifier.fillMaxWidth(0.9f).padding(top = 20.dp, bottom = 20.dp),
            fontSize = 25.sp
        )
        // Spacer(modifier = Modifier.padding(top = 20.dp))
        CustomButton(
            title = TextParameters("Выйти", color = MaterialTheme.colorScheme.onSurface, size = 18),
            text =
                TextParameters(
                    "Выполнить выход из аккаунта",
                    color = MaterialTheme.colorScheme.onSurface.copy(0.85f),
                    size = 14
                ),
            icon =
                IconParameters(
                    value = ImageVector.vectorResource(R.drawable.sign_in),
                    color = errorColor
                ),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
        ) {
            scope.launch(Dispatchers.Main) {
                isLoading.value = true
                logout()
                var timer = 3
                while (timer != 0) {
                    delay(1000)
                    timer -= 1
                }
                isLoading.value = false
                context.startActivity(mainIntent)
                Runtime.getRuntime().exit(0)
            }
        }
    }

    if (isLoading.value) {
        Spinner(text = TextParameters(value = "Выход из аккаунта...", size = 16))
    }
}
