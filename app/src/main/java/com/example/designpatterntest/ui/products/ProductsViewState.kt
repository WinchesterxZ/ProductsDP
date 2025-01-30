package com.example.designpatterntest.ui.products
import com.example.designpatterntest.data.model.Product

sealed class ProductsViewState {
    data object Loading : ProductsViewState() // Loading state

    data class Success(
        val products: List<Product>,
    ) : ProductsViewState()

    data class Message(
        val message: String,
        val action: Action
    ) : ProductsViewState()


    data class Error(
        val type: ErrorType,
        val message: String
    ) : ProductsViewState()

    // Enum to distinguish between different actions
    enum class Action {
        ADDED_TO_FAVORITES, REMOVED_FROM_FAVORITES
    }
    sealed class ErrorType {
        data object LoadError : ErrorType()
        data object DeleteError : ErrorType()
        data object AddToFavoriteError : ErrorType()
    }
}