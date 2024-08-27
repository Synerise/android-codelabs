package com.synerise.integration.app.product

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.gowtham.ratingbar.RatingBar
import com.synerise.integration.app.R
import com.synerise.integration.app.product.storage.model.Product
import com.synerise.integration.app.products.ProductItem
import com.synerise.integration.app.ui.theme.DividerGray
import timber.log.Timber

@Composable
fun ProductScreen(
    viewModel: ProductViewModel = hiltViewModel(),
    onProductClick: (product: Product) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(true) {
        viewModel.getProduct()
        viewModel.getSimilarProducts()
    }

    Scaffold(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    paddingValues = it
                )
                .padding(horizontal = 14.dp),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item(span = { GridItemSpan(2) }) {
                val sidePadding = (-14).dp

                SubcomposeAsyncImage(
                    modifier = Modifier
                        .height(480.dp)
                        .fillMaxWidth()
                        .layout { measurable, constraints ->
                            // Measure the composable adding the side padding*2 (left+right)
                            val placeable =
                                measurable.measure(constraints.offset(horizontal = -sidePadding.roundToPx() * 2))

                            //increase the width adding the side padding*2
                            layout(
                                placeable.width + sidePadding.roundToPx() * 2,
                                placeable.height
                            ) {
                                // Where the composable gets placed
                                placeable.place(+sidePadding.roundToPx(), 0)
                            }
                        },
                    model = uiState.product.image,
                    contentDescription = "Product image",
                    contentScale = ContentScale.Crop,
                    loading = { CircularProgressIndicator() }
                )
            }
            item(span = { GridItemSpan(2) }) {
                Text(
                    text = uiState.product.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black,
                )
            }
            item(span = { GridItemSpan(2) }) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = uiState.product.price.toString(),
                        style = MaterialTheme.typography.displaySmall,
                        color = Color.Black
                    )
                    IconButton(
                        onClick = {
                            viewModel.changeFavouriteState(!uiState.isFavourite)
                        }) {
                        Icon(
                            modifier = Modifier
                                .height(24.dp)
                                .width(24.dp),
                            imageVector = if (uiState.isFavourite) {
                                Icons.Filled.Favorite
                            } else {
                                Icons.Outlined.FavoriteBorder
                            },
                            contentDescription = "Add to favourites",
                            tint = Color.Black
                        )
                    }
                }

            }

            item(span = { GridItemSpan(2) }) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp),
                    shape = RoundedCornerShape(70.dp),
                    onClick = { viewModel.addToCart() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "Add to basket",
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            item(span = { GridItemSpan(2) }) {
                RatingBar(
                    value = uiState.rating,
                    size = 22.29.dp,
                    painterEmpty = painterResource(id = R.drawable.outlined_star),
                    painterFilled = painterResource(id = R.drawable.filled_star),
                    onValueChange = {
                    },
                    onRatingChanged = { ratingChange ->
                        Timber.d("onRatingChanged: $ratingChange")
                        viewModel.updateRating(ratingChange)
                    }
                )
            }

            item(span = { GridItemSpan(2) }) {
                Text(
                    text = uiState.product.description,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Black,
                )
            }

            item(span = { GridItemSpan(2) }) {
                val sidePadding = (-14).dp
                Divider(
                    color = DividerGray, thickness = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .layout { measurable, constraints ->
                            // Measure the composable adding the side padding*2 (left+right)
                            val placeable =
                                measurable.measure(constraints.offset(horizontal = -sidePadding.roundToPx() * 2))

                            //increase the width adding the side padding*2
                            layout(
                                placeable.width + sidePadding.roundToPx() * 2,
                                placeable.height
                            ) {
                                // Where the composable gets placed
                                placeable.place(+sidePadding.roundToPx(), 0)
                            }
                        },
                )
            }

            item(span = { GridItemSpan(2) }) {
                Text(
                    text = "Similar items",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black,
                )
            }

            items(uiState.similarProducts.size) { itemIndex ->
                ProductItem(
                    product = uiState.similarProducts[itemIndex],
                    onProductClick = onProductClick
                )
            }
        }
    }

    uiState.message?.let { message ->
        LaunchedEffect(message) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            viewModel.messageShown()
        }
    }
}