package com.synerise.integration.app.cart

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.synerise.integration.app.LocalUserState
import com.synerise.integration.app.R
import com.synerise.integration.app.UserStateViewModel
import com.synerise.integration.app.product.storage.model.Product
import com.synerise.integration.app.ui.theme.Gray
import com.synerise.integration.app.ui.theme.borderGray

@Composable
fun CartScreen(
    paddingValues: PaddingValues,
    viewModel: CartViewModel = hiltViewModel(),
    onGoToHomeClicked: () -> Unit
) {
    val userState = LocalUserState.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val openAlertDialog = remember { mutableStateOf(false) }

    when {
        openAlertDialog.value -> {
            CustomAlertDialog(
                onConfirmation = {
                    openAlertDialog.value = false
                    onGoToHomeClicked()
                },
                dialogTitle = "Purchase completed",
                dialogText = "A dialog is a type of modal window that appears in front of app content to provide critical information, or prompt for a decision to be made. ",
                icon = Icons.Default.Check
            )
        }
    }

    if (viewModel.areProductsInCartAvailable()) {
        CartProductsList(
            uiState = uiState,
            viewModel = viewModel,
            userState = userState,
            products = uiState.productsInCart,
            paddingValues = paddingValues,
            onPurchaseCompleted = { openAlertDialog.value = true }
        )
    } else {
        EmptyCartScreen(paddingValues)
    }
}

@Composable
fun CartProductsList(
    uiState: CartUiState,
    viewModel: CartViewModel,
    userState: UserStateViewModel,
    paddingValues: PaddingValues,
    products: List<Product>,
    onPurchaseCompleted: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = paddingValues)
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(
                    paddingValues = it
                )
                .padding(horizontal = 15.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Your cart",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            items(products.size) { index ->
                CartProductItem(products[index])
            }

            item {
                Column {
                    Text(
                        text = "Basket amount",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Text(
                        text = "${uiState.basketTotalPrice}",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            item {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(70.dp),
                    onClick = {
                        viewModel.onPurchase()
                        onPurchaseCompleted()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "Complete purchase",
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyCartScreen(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .width(96.dp)
                .height(96.dp),
            painter = painterResource(id = R.drawable.no_items_icon),
            contentDescription = "no items"
        )
        Text(
            text = "Cart is empty",
            style = MaterialTheme.typography.titleLarge,
            color = Color.Black,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = "Go to products and Add to basket",
            style = MaterialTheme.typography.bodyMedium,
            color = Gray,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun CartProductItem(product: Product) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp),
        border = BorderStroke(1.dp, borderGray),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row {
            SubcomposeAsyncImage(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(96.dp),
                model = product.image,
                loading = {
                    CircularProgressIndicator()
                },
                contentDescription = "Product image",
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )
                Text(
                    text = product.price.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(top = 8.dp),
                    color = Gray
                )
            }
        }
    }
}