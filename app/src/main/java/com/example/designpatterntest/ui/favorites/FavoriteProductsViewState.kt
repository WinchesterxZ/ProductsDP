package com.example.designpatterntest.favorites

import com.example.designpatterntest.data.model.Product

sealed class FavoriteProductsViewState {
    data object Loading : FavoriteProductsViewState()
    data class Success(val products: List<Product>) : FavoriteProductsViewState()
    data object Empty : FavoriteProductsViewState()
    data class ProductRemoved(val message: String) : FavoriteProductsViewState()
    data class LoadError(val message: String) : FavoriteProductsViewState()
    data class DeleteError(val message: String) : FavoriteProductsViewState()


}