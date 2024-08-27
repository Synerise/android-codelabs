package com.synerise.integration.app.product.utils

import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.synerise.integration.app.product.storage.model.Product

fun parseProductToJsonString(product: Product): String {
    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val jsonAdapter = moshi.adapter(Product::class.java).lenient()
    return jsonAdapter.toJson(product)
}

fun parseJsonToProduct(jsonProduct: String): Product? {
    return try {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter = moshi.adapter(Product::class.java).lenient()
        jsonAdapter.fromJson(jsonProduct)
    } catch (exception: JsonDataException) {
        null
    }
}