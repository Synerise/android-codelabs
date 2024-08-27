package com.synerise.integration.app.product.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.synerise.integration.app.product.storage.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<Product>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    @Query("DELETE FROM products_table")
    suspend fun deleteProducts()

    @Update
    suspend fun updateProduct(product: Product)

    @Update
    suspend fun updateProducts(products: List<Product>)

    @Query("SELECT * FROM products_table")
    fun getProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products_table WHERE sku = :sku")
    fun getProduct(sku: String): Flow<Product>

    @Query("SELECT * FROM products_table LIMIT 3")
    fun getSimilarProdcts(): Flow<List<Product>>

    @Query("SELECT * FROM products_table WHERE isInCart = 1")
    fun getProductsInCart(): Flow<List<Product>>

    @Query("SELECT * FROM products_table WHERE isFavourite = 1")
    fun getFavouriteProducts(): Flow<List<Product>>
}