package com.synerise.integration.app.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synerise.integration.app.home.topbar.SearchWidgetState
import com.synerise.integration.app.main.synerise.SyneriseRepository
import com.synerise.integration.app.product.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ContainerUiState(
    var cartCount: Int = 0
)

@HiltViewModel
class HomeContainerViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val syneriseRepository: SyneriseRepository
) : ViewModel() {
    private val _searchedWidgetState: MutableState<SearchWidgetState> =
        mutableStateOf(value = SearchWidgetState.CLOSED)
    private val _searchTextState: MutableState<String> = mutableStateOf(value = "")
    val searchWidgetState: State<SearchWidgetState> = _searchedWidgetState
    val searchTextState: State<String> = _searchTextState
    private val _cartCount: MutableStateFlow<Int> = MutableStateFlow(0)

    val uiState: StateFlow<ContainerUiState> = combine(_cartCount) { counts ->
        ContainerUiState(counts.firstOrNull() ?: 0)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ContainerUiState()
        )

    init {
        viewModelScope.launch {
            repository.setupProductsDatabase()
        }
    }

    fun checkCartStatus() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getProductsInCart().collect { productsInCart ->
                _cartCount.value = productsInCart.size
            }
        }
    }

    fun sendScreenVisitEvent(screenIdentifier: String?) {
        screenIdentifier?.let {
            syneriseRepository.sendVisitedScreenEvent(it)
        }
    }
}