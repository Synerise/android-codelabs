package com.synerise.integration.app.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synerise.integration.app.main.synerise.SyneriseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class SignUpUiState(
    var isSignUpSuccessful: Boolean = false,
    var isLoading: Boolean = false,
    var errorMessage: String? = null
)

@HiltViewModel
class SignUpViewModel @Inject constructor(private val syneriseRepository: SyneriseRepository) :
    ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    private var _isSignUpSuccessful = MutableStateFlow(false)
    private var _errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)

    val uiState: StateFlow<SignUpUiState> =
        combine(
            _isSignUpSuccessful,
            _isLoading,
            _errorMessage
        ) { isSignUpSuccessful, isLoading, errorMessage ->
            SignUpUiState(isSignUpSuccessful, isLoading, errorMessage)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = SignUpUiState()
            )

    fun createAccount(
        name: String,
        surname: String,
        email: String,
        password: String,
        push: Boolean,
        marketing: Boolean
    ) {
        _isSignUpSuccessful.value = true
    }

    fun messageShown() {
        _errorMessage.value = null
    }
}