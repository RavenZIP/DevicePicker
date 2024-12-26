package com.ravenzip.devicepicker.ui.screens.main.user.profile

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.extensions.functions.containerColor
import com.ravenzip.devicepicker.extensions.functions.inverseMixColors
import com.ravenzip.devicepicker.ui.theme.errorColor
import com.ravenzip.devicepicker.viewmodels.base.UiEventEffect
import com.ravenzip.devicepicker.viewmodels.main.UserProfileViewModel
import com.ravenzip.workshop.components.AlertDialog
import com.ravenzip.workshop.components.CustomButton
import com.ravenzip.workshop.components.RowIconButton
import com.ravenzip.workshop.data.TextConfig
import com.ravenzip.workshop.data.icon.Icon
import com.ravenzip.workshop.data.icon.IconConfig

@Composable
fun UserProfileScreenContent(
    userProfileViewModel: UserProfileViewModel,
    onClickToAdminPanel: () -> Unit,
    onClickToDeviceHistory: () -> Unit,
    navigateToSplashScreen: () -> Unit,
    padding: PaddingValues,
) {
    val dialogWindowIsShowed =
        userProfileViewModel.alertDialog.isShowed.collectAsStateWithLifecycle(false).value

    val userData = userProfileViewModel.userData.collectAsStateWithLifecycle().value

    Column(
        modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Аккаунт", modifier = Modifier.fillMaxSize(0.9f), fontSize = 18.sp)

        Spacer(modifier = Modifier.padding(top = 10.dp))

        if (userData.admin) {
            RowIconButton(
                text = "Панель администратора",
                textConfig = TextConfig.onSurfaceH2,
                icon = Icon.ResourceIcon(R.drawable.i_rocket),
                iconConfig = IconConfig(color = MaterialTheme.colorScheme.tertiary),
                colors = ButtonDefaults.inverseMixColors(),
            ) {
                onClickToAdminPanel()
            }
            Spacer(modifier = Modifier.padding(top = 15.dp))
        }

        RowIconButton(
            text = "Настройки аккаунта",
            textConfig = TextConfig.onSurfaceH2,
            icon = Icon.ResourceIcon(R.drawable.i_user_settings),
            iconConfig = IconConfig.Primary,
            colors = ButtonDefaults.containerColor(),
        ) {}

        Spacer(modifier = Modifier.padding(top = 15.dp))

        CustomButton(
            title = "История просмотров",
            titleConfig = TextConfig.onSurfaceH2,
            text = "Список просмотренных вами устройств",
            textConfig = TextConfig.onSurface85Small,
            icon = Icon.ResourceIcon(R.drawable.i_history),
            iconConfig = IconConfig.Primary,
            colors = ButtonDefaults.containerColor(),
        ) {
            onClickToDeviceHistory()
        }

        Spacer(modifier = Modifier.padding(top = 15.dp))

        CustomButton(
            title = "Отзывы",
            titleConfig = TextConfig.onSurfaceH2,
            text = "Список оставленных вами отзывов",
            textConfig = TextConfig.onSurface85Small,
            icon = Icon.ResourceIcon(R.drawable.i_feedback),
            iconConfig = IconConfig.Primary,
            colors = ButtonDefaults.containerColor(),
        ) {}

        Spacer(modifier = Modifier.padding(top = 15.dp))

        CustomButton(
            title = "Акции",
            titleConfig = TextConfig.onSurfaceH2,
            text = "Посмотреть информацию об имеющихся акциях и выгодных предложениях",
            textConfig = TextConfig.onSurface85Small,
            icon = Icon.ResourceIcon(R.drawable.i_stocks),
            iconConfig = IconConfig.Primary,
            colors = ButtonDefaults.containerColor(),
        ) {}

        Spacer(modifier = Modifier.padding(top = 15.dp))

        CustomButton(
            title = "Выйти",
            titleConfig = TextConfig.onSurfaceH2,
            text = "Выполнить выход из аккаунта",
            textConfig = TextConfig.onSurface85Small,
            icon = Icon.ResourceIcon(R.drawable.sign_in),
            iconConfig = IconConfig(color = errorColor),
            colors = ButtonDefaults.containerColor(),
        ) {
            userProfileViewModel.alertDialog.show()
        }

        Spacer(modifier = Modifier.padding(top = 20.dp))

        Text(text = "Настройки", modifier = Modifier.fillMaxSize(0.9f), fontSize = 18.sp)

        Spacer(modifier = Modifier.padding(top = 10.dp))

        RowIconButton(
            text = "Внешний вид",
            textConfig = TextConfig.onSurfaceH2,
            icon = Icon.ResourceIcon(R.drawable.i_theme),
            iconConfig = IconConfig.Primary,
            colors = ButtonDefaults.containerColor(),
        ) {}

        Spacer(modifier = Modifier.padding(top = 15.dp))

        CustomButton(
            title = "Уведомления",
            titleConfig = TextConfig.onSurfaceH2,
            text = "Настроить пуш-уведомления и уведомления внутри приложения",
            textConfig = TextConfig.onSurface85Small,
            icon = Icon.ResourceIcon(R.drawable.i_notification),
            iconConfig = IconConfig.Primary,
            colors = ButtonDefaults.containerColor(),
        ) {}

        Spacer(modifier = Modifier.padding(top = 15.dp))

        RowIconButton(
            text = "Обновления",
            textConfig = TextConfig.onSurfaceH2,
            icon = Icon.ResourceIcon(R.drawable.i_update),
            iconConfig = IconConfig.Primary,
            colors = ButtonDefaults.containerColor(),
        ) {}

        Spacer(modifier = Modifier.padding(top = 20.dp))
    }

    UiEventEffect(userProfileViewModel.uiEffect) { event -> navigateToSplashScreen() }

    if (dialogWindowIsShowed) {
        AlertDialog(
            icon = Icon.ResourceIcon(R.drawable.sign_in),
            title = "Выход из аккаунта",
            text = "Вы действительно хотите выполнить выход из аккаунта?",
            onDismissText = "Отмена",
            onConfirmationText = "Выйти",
            onDismiss = { userProfileViewModel.alertDialog.dismiss() },
            onConfirmation = { userProfileViewModel.alertDialog.confirm() },
        )
    }
}
