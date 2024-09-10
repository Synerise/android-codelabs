package com.synerise.integration.app.main.synerise

import android.app.Application
import android.content.Context
import com.synerise.integration.app.product.storage.model.Product
import com.synerise.sdk.core.Synerise
import com.synerise.sdk.core.types.enums.HostApplicationType
import com.synerise.sdk.event.Tracker
import com.synerise.sdk.event.TrackerParams
import com.synerise.sdk.event.model.CustomEvent
import com.synerise.sdk.event.model.interaction.VisitedScreenEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject

class SyneriseRepository @Inject constructor(private val context: Context) {

    private val job = SupervisorJob()
    private val ioScope by lazy { CoroutineScope(job + Dispatchers.IO) }

    fun preSyneriseSdkConfiguration() {
        Synerise.settings.tracker.autoTracking.enabled = false
    }

    fun initializeSynerise() {
        preSyneriseSdkConfiguration()

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
            "Item added to favourites",
            TrackerParams.Builder()
                .add("sku", product.sku)
                .add("isFavourite", product.isFavourite)
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
}
