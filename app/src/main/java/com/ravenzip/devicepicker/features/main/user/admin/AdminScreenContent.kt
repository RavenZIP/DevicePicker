package com.ravenzip.devicepicker.features.main.user.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.FirebaseDatabase
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.components.SinglenessOutlinedTextField
import com.ravenzip.workshop.forms.component.TextFieldComponent
import com.ravenzip.workshop.forms.control.FormControl

@Composable
fun AdminScreenContent(padding: PaddingValues) {
    val key = remember { mutableStateOf("") }
    val firebase = FirebaseDatabase.getInstance()
    val interactionSource = remember { MutableInteractionSource() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val composableScope = rememberCoroutineScope()
    val idComponent = remember {
        TextFieldComponent(control = FormControl(initialValue = ""), scope = composableScope)
    }

    Column(
        modifier =
            Modifier.fillMaxSize()
                .padding(padding)
                .clickable(interactionSource = interactionSource, indication = null) {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Генерация уникального ключа для объекта БД",
            modifier = Modifier.fillMaxWidth(0.9f),
            fontSize = 18.sp,
        )
        Spacer(modifier = Modifier.height(15.dp))

        SinglenessOutlinedTextField(
            component = idComponent,
            label = "Сгенерированный индентификатор",
        )
        Spacer(modifier = Modifier.height(20.dp))

        SimpleButton(text = "Сгенерировать") {
            key.value = firebase.reference.push().key.toString()
        }
        Spacer(modifier = Modifier.height(40.dp))

        Text(text = "Вычисление меток", modifier = Modifier.fillMaxWidth(0.9f), fontSize = 18.sp)
        Spacer(modifier = Modifier.height(15.dp))

        SimpleButton(text = "Выполнить перерасчет меток для всех устройств") {}
        Spacer(modifier = Modifier.height(15.dp))

        SimpleButton(text = "Выполнить перерасчет определенных меток") {}
        Spacer(modifier = Modifier.padding(top = 40.dp))

        Text(
            text = "Добавление объектов в БД",
            modifier = Modifier.fillMaxWidth(0.9f),
            fontSize = 18.sp,
        )
        Spacer(modifier = Modifier.height(15.dp))

        SimpleButton(text = "Добавить категорию") {}
        Spacer(modifier = Modifier.height(15.dp))

        SimpleButton(text = "Добавить бренд") {}
        Spacer(modifier = Modifier.height(15.dp))

        SimpleButton(text = "Добавить устройство") {}
        Spacer(modifier = Modifier.height(40.dp))
    }
}
