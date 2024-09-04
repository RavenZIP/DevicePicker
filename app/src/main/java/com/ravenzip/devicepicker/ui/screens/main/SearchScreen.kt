package com.ravenzip.devicepicker.ui.screens.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravenzip.devicepicker.extensions.functions.veryLightPrimary
import com.ravenzip.devicepicker.viewmodels.main.SearchScreenViewModel
import com.ravenzip.workshop.components.VerticalGrid

@Composable
fun SearchScreen(searchScreenViewModel: SearchScreenViewModel, padding: PaddingValues) {
    val brandList = searchScreenViewModel.brandList.collectAsState().value
    val deviceTypeList = searchScreenViewModel.deviceTypeList.collectAsState().value

    Column(
        modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Категории устройств", modifier = Modifier.fillMaxSize(0.9f), fontSize = 18.sp)

        Spacer(modifier = Modifier.height(10.dp))
        VerticalGrid(items = deviceTypeList) { modifier, item -> BrandCard(modifier, item) }

        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Бренды", modifier = Modifier.fillMaxSize(0.9f), fontSize = 18.sp)

        Spacer(modifier = Modifier.height(10.dp))
        VerticalGrid(items = brandList) { modifier, item -> BrandCard(modifier, item) }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun BrandCard(modifier: Modifier, brandName: String) {
    Card(
        modifier = modifier.clip(RoundedCornerShape(12.dp)).clickable {},
        colors = CardDefaults.veryLightPrimary(),
    ) {
        Text(
            text = brandName,
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W500,
            letterSpacing = 1.sp,
        )
    }
}
