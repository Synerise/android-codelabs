package com.synerise.integration.app.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.synerise.integration.app.LocalUserState
import com.synerise.integration.app.login.LoginViewModel
import com.synerise.integration.app.ui.theme.Gray

@Composable
fun AccountScreen(
    paddingValues: PaddingValues,
    viewModel: AccountViewModel = hiltViewModel()
) {
    val userState = LocalUserState.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
            .padding(paddingValues = paddingValues)
    ) {
        var pushAgreement by remember { mutableStateOf(false) }
        var marketingAgreement by remember { mutableStateOf(false) }

        Text(
            modifier = Modifier.padding(bottom = 16.dp),
            text = "Account",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Switch(
                modifier = Modifier.padding(horizontal = 8.dp),
                checked = marketingAgreement,
                onCheckedChange = { marketingAgreement = it })
            Column {
                Text(
                    text = "Marketing agreements",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Supporting line text lorem ipsum dolor sit amet, nanana.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Gray
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Switch(
                modifier = Modifier.padding(horizontal = 8.dp),
                checked = pushAgreement,
                onCheckedChange = { pushAgreement = it })
            Column {
                Text(
                    text = "Push agreements",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Supporting line text lorem ipsum dolor sit amet, nanana.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Gray
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(70.dp),
            onClick = {
                viewModel.signOut()
                userState.signOut()
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = "Sign Out",
                color = Color.White,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}