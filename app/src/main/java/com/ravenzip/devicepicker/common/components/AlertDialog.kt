package com.ravenzip.devicepicker.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ravenzip.workshop.components.Icon
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.data.TextConfig
import com.ravenzip.workshop.data.icon.IconConfig
import com.ravenzip.workshop.data.icon.IconData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogWithField(
    icon: IconData? = null,
    iconConfig: IconConfig = IconConfig.Default,
    title: String,
    titleConfig: TextConfig = TextConfig.H1,
    text: String,
    textConfig: TextConfig = TextConfig.Small,
    textField: @Composable () -> Unit,
    onDismissText: String,
    onDismissTextConfig: TextConfig = TextConfig.SmallCenteredMedium,
    onConfirmationText: String,
    onConfirmationTextConfig: TextConfig = TextConfig.SmallCenteredMedium,
    containerColors: CardColors = CardDefaults.cardColors(),
    onDismiss: () -> Unit,
    onConfirmation: () -> Unit,
) {
    val titleColor = remember { titleConfig.color ?: Color.Unspecified }
    val textColor = remember { textConfig.color ?: Color.Unspecified }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(shape = RoundedCornerShape(10.dp), colors = containerColors) {
            Column(
                modifier =
                    Modifier.padding(start = 20.dp, end = 20.dp, top = 25.dp, bottom = 25.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                if (icon !== null && iconConfig.size > 0) {
                    Icon(
                        icon = icon,
                        iconConfig = iconConfig,
                        defaultColor = containerColors.contentColor,
                    )
                }

                Text(
                    text = title,
                    color = titleColor,
                    fontSize = titleConfig.size,
                    fontWeight = titleConfig.weight,
                    letterSpacing = titleConfig.letterSpacing,
                )

                Text(
                    text = text,
                    color = textColor,
                    fontSize = textConfig.size,
                    fontWeight = textConfig.weight,
                    letterSpacing = textConfig.letterSpacing,
                )

                textField()

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    SimpleButton(
                        width = 0.5f,
                        text = onDismissText,
                        textConfig = onDismissTextConfig,
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = containerColors.containerColor,
                                contentColor = MaterialTheme.colorScheme.primary,
                            ),
                        contentPadding = PaddingValues(0.dp),
                    ) {
                        onDismiss()
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    SimpleButton(
                        width = 1f,
                        text = onConfirmationText,
                        textConfig = onConfirmationTextConfig,
                        contentPadding = PaddingValues(0.dp),
                    ) {
                        onConfirmation()
                    }
                }
            }
        }
    }
}
