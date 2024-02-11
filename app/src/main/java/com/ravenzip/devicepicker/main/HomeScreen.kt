package com.ravenzip.devicepicker.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(padding: PaddingValues) {
    Column(
        modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Главная",
            modifier = Modifier.fillMaxWidth(0.9f).padding(top = 20.dp, bottom = 20.dp),
            fontSize = 25.sp
        )
        Tovar()
        Spacer(modifier = Modifier.height(20.dp))
        Tovar()
        Spacer(modifier = Modifier.height(20.dp))
        Tovar2()
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun Tovar() {
    Box(
        Modifier.fillMaxWidth(0.9f)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column(modifier = Modifier.padding(top = 15.dp, bottom = 15.dp)) {
            Text(text = "Заголовок", modifier = Modifier.padding(start = 15.dp))
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier =
                        Modifier.padding(start = 15.dp, top = 10.dp, end = 15.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                ) {
                    Column(modifier = Modifier.padding(10.dp).width(50.dp)) {
                        Image(
                            imageVector = Icons.Outlined.Phone,
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "Цена", fontSize = 14.sp)
                    }
                }
                Box(
                    modifier =
                        Modifier.padding(top = 10.dp, end = 15.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                ) {
                    Column(modifier = Modifier.padding(10.dp).width(50.dp)) {
                        Image(
                            imageVector = Icons.Outlined.Phone,
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "Цена", fontSize = 14.sp)
                    }
                }
                Box(
                    modifier =
                        Modifier.padding(top = 10.dp, end = 15.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                ) {
                    Column(modifier = Modifier.padding(10.dp).width(50.dp)) {
                        Image(
                            imageVector = Icons.Outlined.Phone,
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "Цена", fontSize = 14.sp)
                    }
                }
                Box(
                    modifier =
                        Modifier.padding(top = 10.dp, end = 15.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                ) {
                    Column(modifier = Modifier.padding(10.dp).width(50.dp)) {
                        Image(
                            imageVector = Icons.Outlined.Phone,
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "Цена", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun Tovar2() {
    Box(
        Modifier.fillMaxWidth(0.9f)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column(modifier = Modifier.padding(top = 15.dp, start = 15.dp, bottom = 15.dp)) {
            Text(text = "Заголовок")
            Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
                Box(
                    modifier =
                        Modifier.padding(top = 10.dp, end = 15.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                            .weight(1f)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Image(
                            imageVector = Icons.Outlined.Phone,
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth().size(50.dp),
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "Цена", color = MaterialTheme.colorScheme.primary)
                        Text(text = "Название")
                    }
                }
                Box(
                    modifier =
                        Modifier.padding(top = 10.dp, end = 15.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                            .weight(1f)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Image(
                            imageVector = Icons.Outlined.Phone,
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth().size(50.dp),
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "Цена", color = MaterialTheme.colorScheme.primary)
                        Text(text = "Название")
                    }
                }
            }
        }
    }
}
