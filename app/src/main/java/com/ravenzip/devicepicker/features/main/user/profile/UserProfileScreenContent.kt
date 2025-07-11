package com.ravenzip.devicepicker.features.main.user.profile

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
import com.ravenzip.devicepicker.common.theme.errorColor
import com.ravenzip.devicepicker.common.utils.base.UiEventEffect
import com.ravenzip.devicepicker.common.utils.extension.containerColor
import com.ravenzip.devicepicker.common.utils.extension.inverseMixColors
import com.ravenzip.devicepicker.features.main.user.UserProfileViewModel
import com.ravenzip.workshop.components.AlertDialog
import com.ravenzip.workshop.components.CustomButton
import com.ravenzip.workshop.components.RowIconButton
import com.ravenzip.workshop.data.TextConfig
import com.ravenzip.workshop.data.icon.IconConfig
import com.ravenzip.workshop.data.icon.IconData

@Composable
fun UserProfileScreenContent(
    viewModel: UserProfileViewModel,
    navigateToAdminPanel: () -> Unit,
    navigateToUserSettings: () -> Unit,
    navigateToCompany: () -> Unit,
    navigateToDeviceHistory: () -> Unit,
    navigateToReviews: () -> Unit,
    navigateToUserDevices: () -> Unit,
    navigateToVisualAppearance: () -> Unit,
    navigateToUpdates: () -> Unit,
    navigateToSplashScreen: () -> Unit,
    padding: PaddingValues,
) {
    val dialogWindowIsShowed =
        viewModel.alertDialog.isShowed.collectAsStateWithLifecycle(false).value

    val userData = viewModel.userData.collectAsStateWithLifecycle().value

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
                icon = IconData.ResourceIcon(R.drawable.i_rocket),
                iconConfig = IconConfig(color = MaterialTheme.colorScheme.tertiary),
                colors = ButtonDefaults.inverseMixColors(),
            ) {
                navigateToAdminPanel()
            }
            Spacer(modifier = Modifier.padding(top = 15.dp))
        }

        RowIconButton(
            text = "Настройки аккаунта",
            textConfig = TextConfig.onSurfaceH2,
            icon = IconData.ResourceIcon(R.drawable.i_user_settings),
            iconConfig = IconConfig.Primary,
            colors = ButtonDefaults.containerColor(),
        ) {
            navigateToUserSettings()
        }

        Spacer(modifier = Modifier.padding(top = 15.dp))

        CustomButton(
            title = "Компания",
            titleConfig = TextConfig.onSurfaceH2,
            text = "Создать компанию, управлять ранее созданной или вступить в уже существующую",
            textConfig = TextConfig.onSurface85Small,
            icon = IconData.ResourceIcon(R.drawable.i_company),
            iconConfig = IconConfig.Primary,
            colors = ButtonDefaults.containerColor(),
        ) {
            navigateToCompany()
        }

        Spacer(modifier = Modifier.padding(top = 15.dp))

        CustomButton(
            title = "История просмотров",
            titleConfig = TextConfig.onSurfaceH2,
            text = "Список просмотренных вами устройств",
            textConfig = TextConfig.onSurface85Small,
            icon = IconData.ResourceIcon(R.drawable.i_history),
            iconConfig = IconConfig.Primary,
            colors = ButtonDefaults.containerColor(),
        ) {
            navigateToDeviceHistory()
        }

        Spacer(modifier = Modifier.padding(top = 15.dp))

        CustomButton(
            title = "Отзывы",
            titleConfig = TextConfig.onSurfaceH2,
            text = "Список оставленных вами отзывов",
            textConfig = TextConfig.onSurface85Small,
            icon = IconData.ResourceIcon(R.drawable.i_feedback),
            iconConfig = IconConfig.Primary,
            colors = ButtonDefaults.containerColor(),
        ) {
            navigateToReviews()
        }

        Spacer(modifier = Modifier.padding(top = 15.dp))

        CustomButton(
            title = "Мои устройства",
            titleConfig = TextConfig.onSurfaceH2,
            text = "Просмотр устройств, которые принадлежали или принадлежат вам",
            textConfig = TextConfig.onSurface85Small,
            icon = IconData.ResourceIcon(R.drawable.i_devices),
            iconConfig = IconConfig.Primary,
            colors = ButtonDefaults.containerColor(),
        ) {
            navigateToUserDevices()
        }

        Spacer(modifier = Modifier.padding(top = 15.dp))

        CustomButton(
            title = "Выйти",
            titleConfig = TextConfig.onSurfaceH2,
            text = "Выполнить выход из аккаунта",
            textConfig = TextConfig.onSurface85Small,
            icon = IconData.ResourceIcon(R.drawable.sign_in),
            iconConfig = IconConfig(color = errorColor),
            colors = ButtonDefaults.containerColor(),
        ) {
            viewModel.alertDialog.show()
        }

        Spacer(modifier = Modifier.padding(top = 20.dp))

        Text(text = "Настройки", modifier = Modifier.fillMaxSize(0.9f), fontSize = 18.sp)

        Spacer(modifier = Modifier.padding(top = 10.dp))

        RowIconButton(
            text = "Внешний вид",
            textConfig = TextConfig.onSurfaceH2,
            icon = IconData.ResourceIcon(R.drawable.i_theme),
            iconConfig = IconConfig.Primary,
            colors = ButtonDefaults.containerColor(),
        ) {
            navigateToVisualAppearance()
        }

        Spacer(modifier = Modifier.padding(top = 15.dp))

        RowIconButton(
            text = "Обновления",
            textConfig = TextConfig.onSurfaceH2,
            icon = IconData.ResourceIcon(R.drawable.i_update),
            iconConfig = IconConfig.Primary,
            colors = ButtonDefaults.containerColor(),
        ) {
            navigateToUpdates()
        }

        Spacer(modifier = Modifier.padding(top = 20.dp))
    }

    UiEventEffect(viewModel.uiEvent) { event -> navigateToSplashScreen() }

    if (dialogWindowIsShowed) {
        AlertDialog(
            icon = IconData.ResourceIcon(R.drawable.sign_in),
            title = "Выход из аккаунта",
            text = "Вы действительно хотите выполнить выход из аккаунта?",
            onDismissText = "Отмена",
            onConfirmationText = "Выйти",
            onDismiss = { viewModel.alertDialog.dismiss() },
            onConfirmation = { viewModel.alertDialog.confirm() },
        )
    }
}
