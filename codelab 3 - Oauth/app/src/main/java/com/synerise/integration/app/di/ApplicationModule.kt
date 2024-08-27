package com.synerise.integration.app.di

import android.content.Context
import com.google.gson.Gson
import com.synerise.integration.app.main.manager.SessionManager
import com.synerise.integration.app.main.synerise.SyneriseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    fun provideSessionManager(syneriseRepository: SyneriseRepository): SessionManager {
        return SessionManager(syneriseRepository)
    }

    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideSyneriseRepository(context: Context): SyneriseRepository {
        return SyneriseRepository(context)
    }
}