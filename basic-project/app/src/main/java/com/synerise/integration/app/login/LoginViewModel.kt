package com.synerise.integration.app.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class LoginUiState(
    var isLoginSuccessful: Boolean = false,
    var isLoading: Boolean = false,
    var errorMessage: String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    private var _isLoginSuccessful = MutableStateFlow(false)
    private var _errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)

    val uiState: StateFlow<LoginUiState> =
        combine(
            _isLoginSuccessful,
            _isLoading,
            _errorMessage
        ) { isLoginSuccessful, isLoading, errorMessage ->
            LoginUiState(isLoginSuccessful, isLoading, errorMessage)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = LoginUiState()
            )

    fun login(email: String, password: String) {
        _isLoginSuccessful.value = true
    }

    fun messageShown() {
        _errorMessage.value = null
    }
}