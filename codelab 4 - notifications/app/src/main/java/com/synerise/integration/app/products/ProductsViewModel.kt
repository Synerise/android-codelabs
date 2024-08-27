package com.synerise.integration.app.products

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

data class ProductsUiState(
    var products: List<Product> = emptyList(),
    var isLoading: Boolean = false
)
@HiltViewModel
class ProductsViewModel @Inject constructor(private val repository: ProductRepository): ViewModel() {
    private var _products: MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())
    private val _isLoading = MutableStateFlow(false)

    val uiState: StateFlow<ProductsUiState> = combine(_products, _isLoading) { products, isLoading ->
        ProductsUiState(products, isLoading)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ProductsUiState()
        )

    init {
        retrieveProducts()
    }

    fun retrieveProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getProducts().collect {
                _products.value = it
            }
        }
    }
}