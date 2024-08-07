package com.ravenzip.devicepicker.ui.screens.main

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.ravenzip.devicepicker.extensions.functions.containerColor
import com.ravenzip.devicepicker.extensions.functions.inverseMixColors
import com.ravenzip.devicepicker.model.User
import com.ravenzip.devicepicker.ui.theme.errorColor
import com.ravenzip.workshop.components.AlertDialog
import com.ravenzip.workshop.components.CustomButton
import com.ravenzip.workshop.components.RowIconButton
import com.ravenzip.workshop.components.Spinner
import com.ravenzip.workshop.data.IconConfig
import com.ravenzip.workshop.data.TextConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Composable
fun UserProfileScreen(
    padding: PaddingValues,
    userDataByViewModel: StateFlow<User>,
    logout: suspend () -> Unit,
    vararg onClick: () -> Unit
) {
    val isLoading = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val alertDialogIsShown = remember { mutableStateOf(false) }
    val userData = userDataByViewModel.collectAsState().value

    Column(
        modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Аккаунт", modifier = Modifier.fillMaxSize(0.9f), fontSize = 18.sp)

            Spacer(modifier = Modifier.padding(top = 10.dp))

            if (userData.admin) {
                RowIconButton(
                    text =
                        TextConfig(
                            value = "Панель администратора",
                            color = MaterialTheme.colorScheme.onSurface,
                            size = 18),
                    icon =
                        IconConfig(
                            value = ImageVector.vectorResource(R.drawable.i_rocket),
                            color = MaterialTheme.colorScheme.tertiary),
                    colors = ButtonDefaults.inverseMixColors()) {
                        onClick[0]()
                    }
                Spacer(modifier = Modifier.padding(top = 15.dp))
            }

            RowIconButton(
                text =
                    TextConfig(
                        value = "Настройки аккаунта",
                        color = MaterialTheme.colorScheme.onSurface,
                        size = 18),
                icon =
                    IconConfig(
                        value = ImageVector.vectorResource(R.drawable.i_user_settings),
                        color = MaterialTheme.colorScheme.primary),
                colors = ButtonDefaults.containerColor()) {}

            Spacer(modifier = Modifier.padding(top = 15.dp))

            CustomButton(
                title =
                    TextConfig(
                        value = "История просмотров",
                        color = MaterialTheme.colorScheme.onSurface,
                        size = 18),
                text =
                    TextConfig(
                        value = "Список просмотренных вами устройств",
                        color = MaterialTheme.colorScheme.onSurface.copy(0.85f),
                        size = 14),
                icon =
                    IconConfig(
                        value = ImageVector.vectorResource(R.drawable.i_history),
                        color = MaterialTheme.colorScheme.primary),
                colors = ButtonDefaults.containerColor()) {}

            Spacer(modifier = Modifier.padding(top = 15.dp))

            CustomButton(
                title =
                    TextConfig(
                        value = "Отзывы", color = MaterialTheme.colorScheme.onSurface, size = 18),
                text =
                    TextConfig(
                        value = "Список оставленных вами отзывов",
                        color = MaterialTheme.colorScheme.onSurface.copy(0.85f),
                        size = 14),
                icon =
                    IconConfig(
                        value = ImageVector.vectorResource(R.drawable.i_feedback),
                        color = MaterialTheme.colorScheme.primary),
                colors = ButtonDefaults.containerColor()) {}

            Spacer(modifier = Modifier.padding(top = 15.dp))

            CustomButton(
                title =
                    TextConfig(
                        value = "Акции", color = MaterialTheme.colorScheme.onSurface, size = 18),
                text =
                    TextConfig(
                        value = "Посмотреть информацию об имеющихся акциях и выгодных предложениях",
                        color = MaterialTheme.colorScheme.onSurface.copy(0.85f),
                        size = 14),
                icon =
                    IconConfig(
                        value = ImageVector.vectorResource(R.drawable.i_stocks),
                        color = MaterialTheme.colorScheme.primary),
                colors = ButtonDefaults.containerColor()) {}

            Spacer(modifier = Modifier.padding(top = 15.dp))

            CustomButton(
                title = TextConfig("Выйти", color = MaterialTheme.colorScheme.onSurface, size = 18),
                text =
                    TextConfig(
                        value = "Выполнить выход из аккаунта",
                        color = MaterialTheme.colorScheme.onSurface.copy(0.85f),
                        size = 14),
                icon =
                    IconConfig(
                        value = ImageVector.vectorResource(R.drawable.sign_in), color = errorColor),
                colors = ButtonDefaults.containerColor()) {
                    alertDialogIsShown.value = true
                }

            Spacer(modifier = Modifier.padding(top = 20.dp))

            Text(text = "Настройки", modifier = Modifier.fillMaxSize(0.9f), fontSize = 18.sp)

            Spacer(modifier = Modifier.padding(top = 10.dp))

            RowIconButton(
                text =
                    TextConfig(
                        value = "Внешний вид",
                        color = MaterialTheme.colorScheme.onSurface,
                        size = 18),
                icon =
                    IconConfig(
                        value = ImageVector.vectorResource(R.drawable.i_theme),
                        color = MaterialTheme.colorScheme.primary),
                colors = ButtonDefaults.containerColor()) {}

            Spacer(modifier = Modifier.padding(top = 15.dp))

            CustomButton(
                title =
                    TextConfig(
                        value = "Уведомления",
                        color = MaterialTheme.colorScheme.onSurface,
                        size = 18),
                text =
                    TextConfig(
                        value = "Настроить пуш-уведомления и уведомления внутри приложения",
                        color = MaterialTheme.colorScheme.onSurface.copy(0.85f),
                        size = 14),
                icon =
                    IconConfig(
                        value = ImageVector.vectorResource(R.drawable.i_notification),
                        color = MaterialTheme.colorScheme.primary),
                colors = ButtonDefaults.containerColor()) {}

            Spacer(modifier = Modifier.padding(top = 15.dp))

            RowIconButton(
                text =
                    TextConfig(
                        value = "Обновления",
                        color = MaterialTheme.colorScheme.onSurface,
                        size = 18),
                icon =
                    IconConfig(
                        value = ImageVector.vectorResource(R.drawable.i_update),
                        color = MaterialTheme.colorScheme.primary),
                colors = ButtonDefaults.containerColor()) {}

            Spacer(modifier = Modifier.padding(top = 20.dp))
        }

    if (alertDialogIsShown.value) {
        AlertDialog(
            icon = IconConfig(value = ImageVector.vectorResource(R.drawable.sign_in)),
            title = TextConfig(value = "Выход из аккаунта", size = 22),
            text =
                TextConfig(
                    value = "Вы действительно хотите выполнить выход из аккаунта?", size = 14),
            onDismissText = TextConfig(value = "Отмена", size = 14),
            onConfirmationText = TextConfig(value = "Выйти", size = 14),
            onDismiss = { alertDialogIsShown.value = false },
            onConfirmation = {
                scope.launch(Dispatchers.Main) {
                    val packageManager: PackageManager = context.packageManager
                    val intent: Intent? =
                        packageManager.getLaunchIntentForPackage(context.packageName)
                    val componentName: ComponentName? = intent?.component
                    val mainIntent: Intent = Intent.makeRestartActivityTask(componentName)

                    alertDialogIsShown.value = false
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
            })
    }

    if (isLoading.value) {
        Spinner(text = TextConfig(value = "Выход из аккаунта...", size = 16))
    }
}
