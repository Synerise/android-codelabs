package com.synerise.integration.app.main.manager

import com.synerise.integration.app.main.synerise.SyneriseRepository
import javax.inject.Inject

class SessionManager @Inject constructor(private val syneriseRepository: SyneriseRepository) {
    fun isUserLoggedIn(): Boolean {
        return syneriseRepository.isUserSignedIn()
    }
}