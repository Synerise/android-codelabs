package com.synerise.integration.app.home

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.synerise.integration.app.LocalUserState
import com.synerise.integration.app.product.storage.model.Product
import com.synerise.integration.app.products.ProductItem

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onProductClick: (product: Product) -> Unit,
    paddingValues: PaddingValues
) {
    val userState = LocalUserState.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    LazyVerticalGrid(
        modifier = Modifier
            .padding(
                paddingValues = paddingValues
            )
            .padding(horizontal = 15.dp),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item(span = { GridItemSpan(2) }) {
            Text(
                text = "Hello ${userState.userLogin}",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        item(span = { GridItemSpan(2) }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .padding(
                        vertical = 16.dp
                    ),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                Column(modifier = Modifier.padding(start = 40.dp, top = 40.dp)) {
                    Text(
                        text = "Winter",
                        color = MaterialTheme.colorScheme.onTertiary,
                        style = TextStyle(fontSize = 40.sp)
                    )

                    Text(
                        text = "Sale",
                        color = MaterialTheme.colorScheme.onTertiary,
                        style = TextStyle(fontSize = 40.sp)
                    )
                }
            }
        }

        item(span = { GridItemSpan(2) }) { SaleCarousel(offers = listOf("1", "2", "3")) }

        item(span = { GridItemSpan(2) }) {
            Text(
                text = "Recommended products",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        items(uiState.products.size) { index ->
            ProductItem(uiState.products[index], onProductClick)
        }
    }
}

@Composable
fun SaleCarousel(offers: List<String>) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(items = offers) { offer ->
            OfferCard()
        }
    }
}

@Composable
fun OfferCard() {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(220.dp),
        shape = RoundedCornerShape(28.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            SubcomposeAsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .drawWithCache {
                        val gradient = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black),
                            startY = size.height / 3,
                            endY = size.height
                        )
                        onDrawWithContent {
                            drawContent()
                            drawRect(gradient, blendMode = BlendMode.Multiply)
                        }
                    },
                model = "https://upload.snrcdn.net/f2afa4d4d7af216196047d1f7f0613f22a50a8c8/default/origin/a5c82ce95b2449c28c90eabcaf69660d.png",
                contentDescription = "Product image",
                contentScale = ContentScale.Crop,
                loading = { CircularProgressIndicator() }
            )

            Text(
                text = "-10%", style = MaterialTheme.typography.titleMedium, color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(horizontal = 15.dp)
                    .padding(vertical = 10.dp)
            )
        }
    }
}