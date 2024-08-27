package com.synerise.integration.app

import android.app.Application
import com.synerise.integration.app.main.synerise.SyneriseRepository
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class FashionApplication: Application() {

    @Inject
    lateinit var syneriseRepository: SyneriseRepository

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        syneriseRepository.initializeSynerise()
        Timber.d("On Application created")
    }
}