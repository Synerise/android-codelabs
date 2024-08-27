package com.synerise.integration.app.di

import android.content.Context
import com.google.gson.Gson
import com.synerise.integration.app.main.manager.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }
    @Provides
    fun provideSessionManager(): SessionManager {
        return SessionManager()
    }

    @Provides
    fun provideGson(): Gson {
        return Gson()
    }
}