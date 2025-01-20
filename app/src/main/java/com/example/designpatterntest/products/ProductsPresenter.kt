package com.example.designpatterntest.products

import com.example.designpatterntest.network.RetrofitHelper
import com.example.designpatterntest.db.ProductDao
import com.example.designpatterntest.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class ProductsPresenter(
    private val view: ProductsView,
    private val dao: ProductDao
) {
    suspend fun getFavoriteProducts() {
        val products = RetrofitHelper.retrofitService.getProducts()
        val favList = dao.getFavoriteProducts()
        if (products.isSuccessful) {
            withContext(Dispatchers.Main) {
                val productList = products.body()?.products
                if (productList != null) {
                    val favIds = favList.map { it.id }.toSet()
                    productList.forEach {
                        it.isFavorite = it.id in favIds
                    }
                    view.showProducts(productList)
                }
            }
        } else {
            withContext(Dispatchers.Main) {
                view.showMessage(products.message())
            }
        }
    }

    suspend fun addProduct(product: Product) {
        val result = dao.insertProduct(product)
        withContext(Dispatchers.Main) {
            if (result > 0) {
                view.showMessage("Product added to favorites")
            } else {
                view.showMessage("Something went wrong")
            }
        }
    }

    suspend fun deleteProduct(id: Int) {
        val result = dao.deleteProduct(id)
        withContext(Dispatchers.Main) {
            if (result > 0) {
                view.showMessage("Product removed from favorites")
            } else {
                view.showMessage("Something went wrong")
            }
        }
    }

}

