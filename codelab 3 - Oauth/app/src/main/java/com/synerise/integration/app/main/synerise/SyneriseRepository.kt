package com.synerise.integration.app.main.synerise

import android.app.Application
import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.synerise.integration.app.main.model.SyneriseSdkResult
import com.synerise.integration.app.product.storage.model.Product
import com.synerise.sdk.client.Client
import com.synerise.sdk.client.model.ClientIdentityProvider
import com.synerise.sdk.client.model.client.RegisterClient
import com.synerise.sdk.core.Synerise
import com.synerise.sdk.core.types.enums.HostApplicationType
import com.synerise.sdk.error.ApiError
import com.synerise.sdk.event.Tracker
import com.synerise.sdk.event.TrackerParams
import com.synerise.sdk.event.model.CustomEvent
import com.synerise.sdk.event.model.interaction.VisitedScreenEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class SyneriseRepository @Inject constructor(private val context: Context) {

    private val job = SupervisorJob()
    private val ioScope by lazy { CoroutineScope(job + Dispatchers.IO) }
    private lateinit var auth: FirebaseAuth

    fun preSyneriseSdkConfiguration() {
        Synerise.settings.tracker.autoTracking.enabled = false
    }

    fun initializeSynerise() {
        preSyneriseSdkConfiguration()
        auth = Firebase.auth

        Synerise.Builder.with(
            context as Application,
            "YOUR_API_KEY_HERE",
            "CodeLab Android"
        )
            .baseUrl(null)
            .syneriseDebugMode(true)
            .hostApplicationType(HostApplicationType.NATIVE)
            .build()
    }

    fun sendCustomAddToFavouritesProductEvent(product: Product) {
        val event = CustomEvent(
            "favourites.change",
            "fav",
            TrackerParams.Builder()
                .add("sku", product.sku)
                .add("isLiked", product.isFavourite)
                .build()
        )

        Tracker.send(event)
    }

    fun sendVisitedScreenEvent(screenIdentifier: String) {
        // here you can add any useful information
        val screenVisitEvent = VisitedScreenEvent(
            "visit screen",
            TrackerParams.Builder()
                .add("screenIdentifier", screenIdentifier).build()
        )
        Tracker.send(screenVisitEvent)
    }

    fun loginExternallyAndRetrieveToken(
        email: String,
        password: String
    ): Flow<SyneriseSdkResult<String>> =
        callbackFlow {
            // This is internal logging to imitate external user database
            trySend(SyneriseSdkResult.Loading(_data = null))
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser

                        user?.getIdToken(true)?.addOnCompleteListener() { task ->
                            if (task.isSuccessful) {
                                Timber.d("token is: ${task.result.token}")
                                task.result.token?.let { token ->
                                    trySend(SyneriseSdkResult.Success(_data = token))
                                }
                            }
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        trySend(SyneriseSdkResult.Error(ApiError(task.exception!!.fillInStackTrace())))
                    }
                }
            awaitClose { channel.close() }
        }.catch { e ->
            emit(SyneriseSdkResult.Error(ApiError(e)))
        }.flowOn(Dispatchers.IO)

    fun authenticateWithOauth(jwtToken: String): Flow<SyneriseSdkResult<Nothing>> = callbackFlow {
        trySend(SyneriseSdkResult.Loading(_data = null))
        Client.authenticate(
            jwtToken,
            ClientIdentityProvider.OAUTH,
            null,
            null,
            null
        ).execute({ trySend(SyneriseSdkResult.Success(_data = null)) },
            { apiError -> trySend(SyneriseSdkResult.Error(apiError as ApiError)) })
        awaitClose { channel.close() }
    }.catch { e ->
        emit(SyneriseSdkResult.Error(ApiError(e)))
    }.flowOn(Dispatchers.IO)

    fun signOut() {
        Client.signOut()
        auth.signOut()
    }

    fun isUserSignedIn(): Boolean {
        return Client.isSignedIn()
    }

    fun registerClient(registerClient: RegisterClient): Flow<SyneriseSdkResult<Nothing>> =
        callbackFlow {
            trySend(SyneriseSdkResult.Loading(_data = null))
            Client.registerAccount(registerClient)
                .execute({ trySend(SyneriseSdkResult.Success(_data = null)) },
                    { apiError -> trySend(SyneriseSdkResult.Error(apiError as ApiError)) })
            awaitClose { channel.close() }
        }.catch { e ->
            emit(SyneriseSdkResult.Error(ApiError(e)))
        }.flowOn(Dispatchers.IO)
}
