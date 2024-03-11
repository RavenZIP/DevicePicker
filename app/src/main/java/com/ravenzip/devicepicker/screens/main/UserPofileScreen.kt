package com.ravenzip.devicepicker.screens.main

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import com.ravenzip.devicepicker.ui.components.getContainerColor
import com.ravenzip.devicepicker.ui.theme.errorColor
import com.ravenzip.workshop.components.CustomButton
import com.ravenzip.workshop.components.RowIconButton
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

    Column(
        modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Профиль",
            modifier = Modifier.fillMaxWidth(0.9f).padding(top = 20.dp, bottom = 20.dp),
            fontSize = 25.sp
        )

        Text(text = "Аккаунт", modifier = Modifier.fillMaxSize(0.9f))

        Spacer(modifier = Modifier.padding(top = 20.dp))

        RowIconButton(
            text =
                TextParameters(
                    value = "Настройки аккаунта",
                    color = MaterialTheme.colorScheme.onSurface,
                    size = 18
                ),
            icon =
                IconParameters(
                    value = ImageVector.vectorResource(R.drawable.i_user_settings),
                    color = MaterialTheme.colorScheme.primary
                ),
            colors = getContainerColor()
        ) {}

        Spacer(modifier = Modifier.padding(top = 15.dp))

        CustomButton(
            title =
                TextParameters(
                    value = "История просмотров",
                    color = MaterialTheme.colorScheme.onSurface,
                    size = 18
                ),
            text =
                TextParameters(
                    value = "Список просмотренных вами устройств",
                    color = MaterialTheme.colorScheme.onSurface.copy(0.85f),
                    size = 14
                ),
            icon =
                IconParameters(
                    value = ImageVector.vectorResource(R.drawable.i_history),
                    color = MaterialTheme.colorScheme.primary
                ),
            colors = getContainerColor()
        ) {}

        Spacer(modifier = Modifier.padding(top = 15.dp))

        CustomButton(
            title =
                TextParameters(
                    value = "Отзывы",
                    color = MaterialTheme.colorScheme.onSurface,
                    size = 18
                ),
            text =
                TextParameters(
                    value = "Список оставленных вами отзывов",
                    color = MaterialTheme.colorScheme.onSurface.copy(0.85f),
                    size = 14
                ),
            icon =
                IconParameters(
                    value = ImageVector.vectorResource(R.drawable.i_feedback),
                    color = MaterialTheme.colorScheme.primary
                ),
            colors = getContainerColor()
        ) {}

        Spacer(modifier = Modifier.padding(top = 15.dp))

        CustomButton(
            title =
                TextParameters(
                    value = "Акции",
                    color = MaterialTheme.colorScheme.onSurface,
                    size = 18
                ),
            text =
                TextParameters(
                    value = "Посмотреть информацию об имеющихся акциях и выгодных предложениях",
                    color = MaterialTheme.colorScheme.onSurface.copy(0.85f),
                    size = 14
                ),
            icon =
                IconParameters(
                    value = ImageVector.vectorResource(R.drawable.i_stocks),
                    color = MaterialTheme.colorScheme.primary
                ),
            colors = getContainerColor()
        ) {}

        Spacer(modifier = Modifier.padding(top = 15.dp))

        CustomButton(
            title = TextParameters("Выйти", color = MaterialTheme.colorScheme.onSurface, size = 18),
            text =
                TextParameters(
                    value = "Выполнить выход из аккаунта",
                    color = MaterialTheme.colorScheme.onSurface.copy(0.85f),
                    size = 14
                ),
            icon =
                IconParameters(
                    value = ImageVector.vectorResource(R.drawable.sign_in),
                    color = errorColor
                ),
            colors = getContainerColor()
        ) {
            scope.launch(Dispatchers.Main) {
                logoutAndRestart(context = context, isLoading = isLoading)
            }
        }

        Spacer(modifier = Modifier.padding(top = 20.dp))

        Text(text = "Настройки", modifier = Modifier.fillMaxSize(0.9f))

        Spacer(modifier = Modifier.padding(top = 20.dp))

        RowIconButton(
            text =
                TextParameters(
                    value = "Внешний вид",
                    color = MaterialTheme.colorScheme.onSurface,
                    size = 18
                ),
            icon =
                IconParameters(
                    value = ImageVector.vectorResource(R.drawable.i_theme),
                    color = MaterialTheme.colorScheme.primary
                ),
            colors = getContainerColor()
        ) {}

        Spacer(modifier = Modifier.padding(top = 15.dp))

        CustomButton(
            title =
                TextParameters(
                    value = "Уведомления",
                    color = MaterialTheme.colorScheme.onSurface,
                    size = 18
                ),
            text =
                TextParameters(
                    value = "Настроить пуш-уведомления и уведомления внутри приложения",
                    color = MaterialTheme.colorScheme.onSurface.copy(0.85f),
                    size = 14
                ),
            icon =
                IconParameters(
                    value = ImageVector.vectorResource(R.drawable.i_notification),
                    color = MaterialTheme.colorScheme.primary
                ),
            colors = getContainerColor()
        ) {}

        Spacer(modifier = Modifier.padding(top = 15.dp))

        RowIconButton(
            text =
                TextParameters(
                    value = "Обновления",
                    color = MaterialTheme.colorScheme.onSurface,
                    size = 18
                ),
            icon =
                IconParameters(
                    value = ImageVector.vectorResource(R.drawable.i_update),
                    color = MaterialTheme.colorScheme.primary
                ),
            colors = getContainerColor()
        ) {}

        Spacer(modifier = Modifier.padding(top = 20.dp))
    }

    if (isLoading.value) {
        Spinner(text = TextParameters(value = "Выход из аккаунта...", size = 16))
    }
}

suspend fun logoutAndRestart(context: Context, isLoading: MutableState<Boolean>) {
    val packageManager: PackageManager = context.packageManager
    val intent: Intent? = packageManager.getLaunchIntentForPackage(context.packageName)
    val componentName: ComponentName? = intent?.component
    val mainIntent: Intent = Intent.makeRestartActivityTask(componentName)

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
