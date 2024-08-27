package com.synerise.integration.app.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synerise.integration.app.product.ProductRepository
import com.synerise.integration.app.product.storage.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FavouritesUiState(
    var favourites: List<Product> = emptyList(),
    var isLoading: Boolean = false
)
@HiltViewModel
class FavouritesViewModel @Inject constructor(private val repository: ProductRepository): ViewModel() {
    private var _favourites: MutableStateFlow<List<Product>> = MutableStateFlow(mutableListOf())
    private val _isLoading = MutableStateFlow(false)

    val uiState: StateFlow<FavouritesUiState> = combine(_favourites, _isLoading) { favourites, isLoading ->
        FavouritesUiState(favourites, isLoading)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = FavouritesUiState()
        )

    init {
        getFavouriteProducts()
    }

    fun getFavouriteProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getFavouriteProducts().collect{ favouriteProducts ->
                _favourites.value = favouriteProducts
            }
        }
    }

    fun areFavouriteProductsAvailable(): Boolean {
       return _favourites.value.isNotEmpty()
    }

    fun deleteFromFavourites(product: Product) {
        product.isFavourite = false
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateProduct(product)
        }
    }
}