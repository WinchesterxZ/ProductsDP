package com.example.designpatterntest.data.repository

import android.util.Log
import com.example.designpatterntest.data.db.ProductDao
import com.example.designpatterntest.data.model.Product
import com.example.designpatterntest.data.network.ApiService
import retrofit2.HttpException
import java.io.IOException

class ProductsRepository(
    private val apiService: ApiService,
    private val productDao: ProductDao
) {

    private suspend fun fetchProductsFromApi(): List<Long> {
        val response = apiService.getProducts()
        if (response.isSuccessful) {
            val products = response.body()?.products ?: emptyList()
            return productDao.insertProducts(products)
        } else {
            throw HttpException(response)
        }
    }

    suspend fun getProducts(): List<Product> {
        return try {
            // Try fetching fresh data from the API
            val insertedRows = fetchProductsFromApi()
            if (insertedRows.isNotEmpty()) {
                Log.d("alo", "getProducts: +${insertedRows.size} ")
                productDao.getAllProducts() // Return updated data from DB
            } else {
                throw IOException("Failed to insert products into the database.")
            }
        } catch (e: Exception) {
            // If API call or insertion fails, use cached data
            val localProducts = productDao.getAllProducts()
            if (localProducts.isNotEmpty()) {
                return localProducts
            } else {
                // If no local data is available, propagate the error
                throw IOException("No products available locally or from the API. Error: ${e.message}")
            }
        }
    }


    suspend fun getFavoriteProducts(): List<Product> {
        return productDao.getFavoriteProducts()
    }

    suspend fun addProduct(product: Product): Long {
        return productDao.insertProduct(product)
    }

    suspend fun deleteProduct(id: Int): Int {
        return productDao.deleteProduct(id)
    }

}