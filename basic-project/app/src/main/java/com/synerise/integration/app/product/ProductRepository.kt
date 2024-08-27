package com.synerise.integration.app.product

import android.content.Context
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import com.synerise.integration.app.main.utils.ReadJSONFromAssets
import com.synerise.integration.app.product.storage.ProductDao
import com.synerise.integration.app.product.storage.model.Product
import com.synerise.integration.app.product.storage.model.ProductDataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val productDao: ProductDao,
    private val context: Context, private val gson: Gson
) {

    fun setupProductsDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            getProducts().collect { products ->
                if (products.isEmpty()) {
                    readProductsFromJson()
                }
            }
        }
    }

    suspend fun readProductsFromJson() {
        val jsonString = ReadJSONFromAssets(context, "data.json")
        val productDataModelList =
            gson.fromJson(jsonString, Array<ProductDataModel>::class.java).asList()
        val temporary = mutableListOf<Product>()
        for ((index, value) in productDataModelList.withIndex()) {
            if (index < 20) {
                val product = Product(
                    value.sku,
                    value.brand,
                    value.name,
                    value.category,
                    value.image,
                    value.price.toDouble(),
                    0.0,
                    false,
                    isInCart = false,
                    cartQuantity = 0
                )
                temporary.add(product)
            }
        }

        productDao.insertProducts(temporary)
    }

    fun getProducts() = productDao.getProducts()

    fun getProduct(sku: String) = productDao.getProduct(sku)

    suspend fun addProducts(products: List<Product>) {
        productDao.insertProducts(products)
    }

    suspend fun updateProduct(product: Product) {
        productDao.updateProduct(product)
    }

    suspend fun updateProducts(products: List<Product>) {
        productDao.updateProducts(products)
    }

    fun getSimilarProducts() = productDao.getSimilarProdcts()

    fun getProductsInCart() = productDao.getProductsInCart()

    fun getFavouriteProducts() = productDao.getFavouriteProducts()
}