package com.example.designpatterntest.products
import com.example.designpatterntest.data.model.Product

sealed class ProductsViewState{
    data object Loading : ProductsViewState()
    data class Success(val products: List<Product>) : ProductsViewState()
    data class ProductRemoved(val message: String) : ProductsViewState()
    data class ProductAdded(val message: String) : ProductsViewState()
    data class LoadError(val message: String) : ProductsViewState()
    data class DeleteError(val message: String) : ProductsViewState()
    data class AddToFavoriteError(val message: String) : ProductsViewState()

}