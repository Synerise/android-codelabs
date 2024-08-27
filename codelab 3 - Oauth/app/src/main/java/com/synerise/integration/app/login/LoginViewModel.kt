package com.synerise.integration.app.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synerise.integration.app.main.model.SyneriseSdkResult
import com.synerise.integration.app.main.synerise.SyneriseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class LoginUiState(
    var isLoginSuccessful: Boolean = false,
    var isLoading: Boolean = false,
    var errorMessage: String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val syneriseRepository: SyneriseRepository
) : ViewModel() {
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
        viewModelScope.launch {
            _isLoading.value = true
            syneriseRepository.loginExternallyAndRetrieveToken(email, password).collect { result ->
                when (result.status) {
                    SyneriseSdkResult.Status.SUCCESS -> {
                        result.data?.let { jwtToken ->
                            syneriseRepository.authenticateWithOauth(jwtToken).collect { result ->
                                when (result.status) {
                                    SyneriseSdkResult.Status.SUCCESS -> {
                                        Timber.d("Authenticated by oauth successfully")
                                        _isLoginSuccessful.value = true
                                    }
                                    SyneriseSdkResult.Status.ERROR -> {
                                        Timber.d("Error while authenticating : ${result.error?.errorBody?.message}")
                                        val message = result.error?.errorBody?.message
                                        message?.let {
                                            _errorMessage.value = it
                                        }
                                        _isLoading.value = false
                                    }

                                    SyneriseSdkResult.Status.LOADING -> {}
                                }
                            }
                        }
                    }
                    SyneriseSdkResult.Status.ERROR -> {
                        Timber.d("Error while retrieving token : ${result.error?.errorBody?.message}")
                        val message = result.error?.errorBody?.message
                        message?.let {
                            _errorMessage.value = it
                        }
                        _isLoading.value = false
                    }
                    SyneriseSdkResult.Status.LOADING -> {}
                }
            }
        }
    }

    fun messageShown() {
        _errorMessage.value = null
    }
}