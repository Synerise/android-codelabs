package com.synerise.integration.app.di

import android.content.Context
import com.synerise.integration.app.product.storage.ProductDao
import com.synerise.integration.app.product.storage.ProductDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ProductDatabase =
        ProductDatabase.create(context)

    @Provides
    fun provideDao(database: ProductDatabase): ProductDao {
        return database.productDao()
    }
}