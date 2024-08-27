package com.synerise.integration.app.product.storage.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products_table")
data class Product(
    @PrimaryKey
    val sku: String,
    val brand: String,
    val name: String,
    val description: String,
    val image: String,
    val price: Double,
    var rating: Double = 0.0,
    var isFavourite: Boolean = false,
    var isInCart: Boolean = false,
    var cartQuantity: Int = 0
)


