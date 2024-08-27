package com.synerise.integration.app.favourites

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RemoveCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.synerise.integration.app.R
import com.synerise.integration.app.home.HomeContainerScaffold
import com.synerise.integration.app.home.HomeContainerViewModel
import com.synerise.integration.app.product.storage.model.Product
import com.synerise.integration.app.ui.theme.Gray

@Composable
fun FavouritesScreen(
    viewModel: FavouritesViewModel = hiltViewModel(),
    paddingValues: PaddingValues
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (viewModel.areFavouriteProductsAvailable()) {
        ProductFavouritesGrid(
            uiState.favourites,
            paddingValues,
            onProductRemoveFromFavourites = { product ->
                viewModel.deleteFromFavourites(product)
            })
    } else {
        EmptyFavouritesScreen(paddingValues)
    }
}

@Composable
fun ProductFavouritesGrid(
    products: List<Product>,
    paddingValues: PaddingValues,
    onProductRemoveFromFavourites: (product: Product) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .padding(paddingValues = paddingValues),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)

    ) {

        item(span = { GridItemSpan(2) }) {
            Text(
                text = "Favourite",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        items(products.size) {
            ProductFavouritesItem(products[it], onProductRemoveFromFavourites)
        }
    }
}

@Composable
fun ProductFavouritesItem(product: Product, onProductRemoveFromFavourites: (product: Product) -> Unit) {
    OutlinedCard(
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(308.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                SubcomposeAsyncImage(
                    modifier = Modifier
                        .height(240.dp),
                    model = product.image,
                    contentDescription = "Product image",
                    contentScale = ContentScale.Crop,
                    loading = { CircularProgressIndicator() }
                )

                IconButton(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 10.dp, end = 10.dp),
                    onClick = { onProductRemoveFromFavourites(product) }) {
                    Icon(
                        modifier = Modifier
                            .height(24.dp)
                            .width(24.dp),
                        imageVector = Icons.Outlined.RemoveCircle,
                        contentDescription = "Remove circle",
                        tint = Color.Black
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .padding(top = 8.dp),
                    color = Color.Black
                )
                Text(
                    text = product.price.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .padding(top = 4.dp, bottom = 8.dp),
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun EmptyFavouritesScreen(paddingValues: PaddingValues) {
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
            text = "No favourite items",
            style = MaterialTheme.typography.titleLarge,
            color = Color.Black,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = "Find your favourite products",
            style = MaterialTheme.typography.bodyMedium,
            color = Gray,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
