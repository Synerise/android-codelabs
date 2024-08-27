package com.synerise.integration.app.main.notifications

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.synerise.integration.app.main.model.SyneriseSdkResult
import com.synerise.integration.app.main.synerise.SyneriseRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService :
    FirebaseMessagingService() {

    private val job = SupervisorJob()
    private val ioScope by lazy { CoroutineScope(job + Dispatchers.IO) }
    @Inject
    lateinit var repository: SyneriseRepository


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("New firebase token is: $token")
        ioScope.launch {
            repository.registerForPush(token).collect { result ->
                when (result) {
                    is SyneriseSdkResult.Error -> {
                        Timber.d("Error while sending Firebase token to synerise")
                    }

                    is SyneriseSdkResult.Loading -> {}
                    is SyneriseSdkResult.Success -> Timber.d("Firebase token successfully registered in synerise")
                }
            }
        }

    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        repository.handleNotification(message)
    }
}