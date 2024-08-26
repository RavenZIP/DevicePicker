package com.ravenzip.devicepicker.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.constants.enums.OperationErrorTypeEnum
import com.ravenzip.devicepicker.constants.enums.StatusEnum
import com.ravenzip.devicepicker.viewmodels.UserViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    destroySplashScreen: () -> Unit,
    navigateToAuthentication: () -> Unit,
    navigateToMain: () -> Unit,
    userViewModel: UserViewModel,
) {
    val text = remember { mutableStateOf("Обновление данных о пользователе") }
    val status = remember { mutableStateOf(StatusEnum.LOADING) }

    LaunchedEffect(Unit) {
        val reloadResult = userViewModel.reloadUser()

        if (reloadResult.value == true) {
            status.value = StatusEnum.OK
            text.value = "Загрузка данных завершена"
            destroySplashScreen()
            delay(500)

            if (userViewModel.getUser() !== null) {
                navigateToMain()
            } else {
                navigateToAuthentication()
            }
        } else {
            status.value = StatusEnum.ERROR

            if (reloadResult.error?.type == OperationErrorTypeEnum.NETWORK_ERROR) {
                text.value = "Ошибка сети. Попробуйте позднее"
            } else {
                text.value = "Произошла неизвестная ошибка"
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon),
            contentDescription = "",
            modifier = Modifier.size(300.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
        )
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Row(
            modifier = Modifier.fillMaxWidth(0.9f).padding(bottom = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            when (status.value) {
                StatusEnum.LOADING -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 2.dp,
                    )
                }

                StatusEnum.OK -> {
                    Icon(
                        painter = painterResource(R.drawable.i_success),
                        contentDescription = "",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }

                StatusEnum.ERROR -> {
                    Icon(
                        painter = painterResource(R.drawable.i_error),
                        contentDescription = "",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = text.value,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.W500,
                letterSpacing = 0.sp,
            )
        }
    }
}
