package com.synerise.integration.app.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synerise.integration.app.main.synerise.SyneriseRepository
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

data class ProductUiState(
    var product: Product = Product("", "", "", "", "", 0.0),
    var isFavourite: Boolean = false,
    var rating: Float = 0F,
    var similarProducts: List<Product> = emptyList(),
    var message: String? = null
)

@HiltViewModel
class ProductViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: ProductRepository,
    private val syneriseRepository: SyneriseRepository
) : ViewModel() {
    private val sku: String = checkNotNull(savedStateHandle["sku"])
    private var _product: MutableStateFlow<Product> =
        MutableStateFlow(Product("", "", "", "", "", 0.0, 0.0))
    private var _similarProducts: MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())
    private val _rating = MutableStateFlow(0F)
    private var _isFavourite = MutableStateFlow(false)
    private val _message: MutableStateFlow<String?> = MutableStateFlow(null)

    val uiState: StateFlow<ProductUiState> =
        combine(_product, _similarProducts, _rating, _isFavourite, _message) { product, similar, rating, isFavourite, message ->
            ProductUiState(product, isFavourite, rating, similar, message)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = ProductUiState()
            )

    fun getProduct() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getProduct(sku).collect {
                _product.value = it
                _rating.value = it.rating.toFloat()
                _isFavourite.value = it.isFavourite
            }
        }
    }

    fun getSimilarProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getSimilarProducts().collect {
                _similarProducts.value = it
            }
        }
    }

    fun updateRating(rating: Float) {
        _product.value.rating = rating.toDouble()
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateProduct(_product.value)
        }
    }

    fun addToCart() {
        _message.value = "Product added to cart"
        _product.value.isInCart = true
        _product.value.cartQuantity += 1
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateProduct(_product.value)
        }
    }

    fun changeFavouriteState(newState: Boolean) {
        _product.value.isFavourite = newState
        syneriseRepository.sendCustomAddToFavouritesProductEvent(_product.value)
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateProduct(_product.value)
        }
    }

    fun messageShown() {
        _message.value = null
    }
}