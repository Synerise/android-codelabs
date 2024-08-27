package com.synerise.integration.app.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synerise.integration.app.main.model.SyneriseSdkResult
import com.synerise.integration.app.main.synerise.SyneriseRepository
import com.synerise.sdk.client.model.client.Agreements
import com.synerise.sdk.client.model.client.RegisterClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
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
        val agreements = Agreements()
        agreements.push = push
        agreements.email = marketing
        val registerClient =
            RegisterClient().setEmail(email).setFirstName(name).setLastName(surname)
                .setPassword(password).setAgreements(
                    agreements
                )
        viewModelScope.launch {
            syneriseRepository.registerClient(registerClient).collect { result ->
                when (result.status) {
                    SyneriseSdkResult.Status.SUCCESS -> {
                        Timber.d("Account created successfully")
                        _isSignUpSuccessful.value = true
                    }

                    SyneriseSdkResult.Status.ERROR -> {
                        Timber.d("Error while creating account")
                        val message =
                            result.error?.errorBody?.message
                        message?.let {
                            _errorMessage.value = it
                        }
                        _isLoading.value = false
                    }

                    SyneriseSdkResult.Status.LOADING -> {
                        _isLoading.value = true
                    }
                }
            }
        }
    }

    fun messageShown() {
        _errorMessage.value = null
    }
}