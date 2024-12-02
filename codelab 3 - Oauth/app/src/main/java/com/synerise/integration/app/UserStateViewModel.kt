package com.synerise.integration.app

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.synerise.integration.app.main.manager.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserStateViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {
    var isLoggedIn by mutableStateOf(false)
    var userLogin by mutableStateOf("")

    init {
        isLoggedIn = sessionManager.isUserLoggedIn()
    }

    fun signIn(login: String) {
        userLogin = login
        isLoggedIn = true
    }

    fun signOut() {
        userLogin = ""
        isLoggedIn = false
    }
}

val LocalUserState = compositionLocalOf<UserStateViewModel> { error("User state context not found") }