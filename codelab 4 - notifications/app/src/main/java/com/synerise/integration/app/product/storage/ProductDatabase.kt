package com.synerise.integration.app.product.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.synerise.integration.app.product.storage.model.Product

private const val DB_NAME = "product_database"

@Database(entities = [(Product::class)], version = 1)
abstract class ProductDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object {
        fun create(context: Context): ProductDatabase {

            return Room.databaseBuilder(
                context,
                ProductDatabase::class.java,
                DB_NAME
            ).build()
        }
    }
}
