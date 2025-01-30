package com.example.designpatterntest.ui.favorites

import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.designpatterntest.data.model.Product
import com.example.designpatterntest.data.repository.ProductsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoriteProductsViewModel(
    private val productsRepository: ProductsRepository
): ViewModel() {
    private val _viewState = MutableStateFlow<FavoriteProductsViewState>(FavoriteProductsViewState.Loading)
    val viewState =  _viewState.asStateFlow()




     fun getFavoriteProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("getFavoriteProducts", "feting Data: ")
            try {
                val products = productsRepository.getFavoriteProducts()
                if (products.isEmpty()){
                    _viewState.emit(FavoriteProductsViewState.Empty)
                    Log.d("getFavoriteProducts", "emptyState View Model ")
                }else{
                    _viewState.emit(FavoriteProductsViewState.Success(products))
                }
            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is SQLiteException -> "Database error. Please try again."
                    else -> "An unexpected error occurred: ${e.message}"
                }
                _viewState.emit(FavoriteProductsViewState.LoadError(errorMessage))

            }
        }
    }
    fun deleteProduct(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = productsRepository.deleteProduct(id)
                if (result > 0) {
                    Log.d("getFavoriteProducts", "Emitting ProductRemoved state")
                    _viewState.emit( FavoriteProductsViewState.ProductRemoved("Product removed from favorites"))
                    Log.d("getFavoriteProducts", "Success Delete")
                } else {
                    _viewState.emit(FavoriteProductsViewState.DeleteError("Failed to remove product"))

                }
            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is SQLiteException -> "Database error. Please try again."
                    else -> "An unexpected error occurred: ${e.message}"
                }
                _viewState.emit(FavoriteProductsViewState.DeleteError(errorMessage))
            }
        }
    }
    fun toggleFavButton(product: Product){
        product.isFavorite = !product.isFavorite
    }

}