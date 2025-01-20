package com.example.designpatterntest.favorites


import com.example.designpatterntest.db.ProductDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class FavoritePresenter(
    private val view: FavoritesView,
    private val dao: ProductDao
) {
    suspend fun getFavoriteProducts() {
        val products = dao.getFavoriteProducts()
        withContext(Dispatchers.Main) {
            view.getFavoriteProducts(products)

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
        getFavoriteProducts()
    }
}