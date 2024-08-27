package com.synerise.integration.app.cart

import android.util.Log
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

data class CartUiState(
    var productsInCart: List<Product> = emptyList(),
    var isLoading: Boolean = false,
    var basketTotalPrice: Double = 0.0
)
@HiltViewModel
class CartViewModel @Inject constructor(private val repository: ProductRepository): ViewModel() {
    private var _productsInCart: MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())
    private var _basketTotalPrice: MutableStateFlow<Double> = MutableStateFlow(0.0)
    private val _isLoading = MutableStateFlow(false)

    val uiState: StateFlow<CartUiState> = combine(_productsInCart, _isLoading, _basketTotalPrice) { productsInCart, isLoading, basketTotalPrice ->
        CartUiState(productsInCart, isLoading, basketTotalPrice)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = CartUiState()
        )

    init {
        getCartProducts()
    }

    fun getCartProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getProductsInCart().collect {
                _productsInCart.value = it
                var tempTotalPrice = 0.0
                it.forEach { product ->
                    tempTotalPrice += product.price
                }
                _basketTotalPrice.value += tempTotalPrice
            }
        }
    }

    fun onPurchase() {
        viewModelScope.launch(Dispatchers.IO) {
            _productsInCart.value.forEach { product ->
                product.isInCart = false
                product.cartQuantity = 0
            }
            repository.updateProducts(_productsInCart.value)
        }
    }

    fun areProductsInCartAvailable(): Boolean {
        return _productsInCart.value.isNotEmpty()
    }
}