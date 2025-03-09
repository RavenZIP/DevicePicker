package com.ravenzip.devicepicker.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ravenzip.devicepicker.common.theme.RoundedTop

@Composable
fun BottomContainer(
    padding: PaddingValues = PaddingValues(20.dp),
    content: @Composable () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Column(
            modifier =
                Modifier.fillMaxWidth()
                    .clip(RoundedTop)
                    .background(MaterialTheme.colorScheme.surfaceContainer),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(padding.calculateTopPadding()))
            content()
            Spacer(modifier = Modifier.height(padding.calculateBottomPadding()))
        }
    }
}

// TODO перейти впоследствии на этот контейнер, а в местах использования BottomContainer
// использовать Scaffold,
// потому что начиная с 15 версии андроид он работает неправильно
@Composable
fun BottomContainer2(
    padding: PaddingValues = PaddingValues(20.dp),
    content: @Composable () -> Unit,
) {
    Column(
        modifier =
            Modifier.fillMaxWidth()
                .clip(RoundedTop)
                .background(MaterialTheme.colorScheme.surfaceContainer),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(padding.calculateTopPadding()))
        content()
        Spacer(modifier = Modifier.height(padding.calculateBottomPadding()))
    }
}
