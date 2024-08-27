package com.synerise.integration.app.account

import androidx.lifecycle.ViewModel
import com.synerise.integration.app.main.synerise.SyneriseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(private val repository: SyneriseRepository) :
    ViewModel() {

    fun signOut() {

    }
}