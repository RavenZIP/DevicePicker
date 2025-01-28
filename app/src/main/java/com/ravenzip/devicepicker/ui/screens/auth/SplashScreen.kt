package com.ravenzip.devicepicker.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.state.UiState
import com.ravenzip.devicepicker.ui.components.CenterRow
import com.ravenzip.devicepicker.viewmodels.auth.SplashViewModel

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    navigateToAuthentication: () -> Unit,
    navigateToMain: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.i_devicepicker),
            contentDescription = "",
            modifier = Modifier.size(150.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Device Picker",
            fontSize = 25.sp,
            fontWeight = FontWeight.W500,
            color = MaterialTheme.colorScheme.primary,
        )
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        CenterRow(modifier = Modifier.fillMaxWidth(0.9f).padding(bottom = 15.dp)) {
            when (uiState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 2.dp,
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.W500,
                        letterSpacing = 0.sp,
                    )
                }

                is UiState.Success -> {
                    Icon(
                        painter = painterResource(R.drawable.i_success),
                        contentDescription = "",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = uiState.data,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.W500,
                        letterSpacing = 0.sp,
                    )
                }

                is UiState.Error -> {
                    Icon(
                        painter = painterResource(R.drawable.i_error),
                        contentDescription = "",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.W500,
                        letterSpacing = 0.sp,
                    )
                }
            }
        }
    }

    LaunchedEffect(uiState) {
        if (uiState is UiState.Success) {
            if (viewModel.firebaseUser !== null) navigateToMain() else navigateToAuthentication()
        }
    }
}
