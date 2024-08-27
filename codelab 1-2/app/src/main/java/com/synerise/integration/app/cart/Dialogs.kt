package com.synerise.integration.app.cart

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.synerise.integration.app.ui.theme.Gray

@Composable
fun CustomAlertDialog(
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(
                icon,
                modifier = Modifier
                    .height(24.dp)
                    .width(24.dp),
                contentDescription = "Example Icon"
            )
        },
        title = {
            Text(
                text = dialogTitle,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black
            )
        },
        text = {
            Text(
                text = dialogText,
                style = MaterialTheme.typography.bodyMedium,
                color = Gray
            )
        },
        onDismissRequest = {},
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(
                    text = "Go to homepage",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}