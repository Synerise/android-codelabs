package com.synerise.integration.app.products

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.synerise.integration.app.product.storage.model.Product
import com.synerise.integration.app.ui.theme.DividerGray

@Composable
fun ProductsScreen(
    viewModel: ProductsViewModel = hiltViewModel(),
    onProductClick: (product: Product) -> Unit,
    paddingValues: PaddingValues
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LazyVerticalGrid(
        modifier = Modifier
            .padding(
                paddingValues = paddingValues
            )
            .padding(horizontal = 12.dp),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item(span = { GridItemSpan(2) }) {
            Text(
                text = "Products",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        items(uiState.products.size) { index ->
            ProductItem(uiState.products[index], onProductClick)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductItem(
    product: Product,
    onProductClick: (product: Product) -> Unit
) {
    OutlinedCard(
        shape = RoundedCornerShape(28.dp),
        onClick = { onProductClick(product) },
        border = BorderStroke(1.dp, DividerGray),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(308.dp)
        ) {
            SubcomposeAsyncImage(
                modifier = Modifier
                    .height(240.dp),
                model = product.image,
                contentDescription = "Product image",
                contentScale = ContentScale.Crop,
                loading = { Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                } }
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(vertical = 10.dp)
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black
                )
                Text(
                    text = product.price.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(top = 4.dp),
                    color = Color.Black
                )
            }
        }
    }
}