package com.example.designpatterntest.favorites

import android.database.sqlite.SQLiteException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.designpatterntest.data.db.ProductDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteProductsViewModel(
    private val productDao: ProductDao

): ViewModel() {
    private val _viewState = MutableLiveData<FavoriteProductsViewState>()
    val viewState: LiveData<FavoriteProductsViewState> = _viewState



     fun getFavoriteProducts() {
        _viewState.value = FavoriteProductsViewState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val products = productDao.getFavoriteProducts()
                withContext(Dispatchers.Main) {
                    if (products.isEmpty()){
                        _viewState.value = FavoriteProductsViewState.Empty
                    }else{
                        _viewState.value = FavoriteProductsViewState.Success(products)
                    }
                }
            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is SQLiteException -> "Database error. Please try again."
                    else -> "An unexpected error occurred: ${e.message}"
                }
                withContext(Dispatchers.Main){
                    _viewState.value = FavoriteProductsViewState.LoadError(errorMessage)
                }
            }
        }
    }
    fun deleteProduct(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = productDao.deleteProduct(id)
                withContext(Dispatchers.Main) {
                    if (result > 0) {
                        _viewState.value = FavoriteProductsViewState.ProductRemoved("Product removed from favorites")
                        getFavoriteProducts() // Refresh the list
                    } else {
                        _viewState.value = FavoriteProductsViewState.DeleteError("Failed to remove product")
                    }
                }
            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is SQLiteException -> "Database error. Please try again."
                    else -> "An unexpected error occurred: ${e.message}"
                }
                withContext(Dispatchers.Main) {
                    _viewState.value = FavoriteProductsViewState.DeleteError(errorMessage)
                }
            }
        }
    }


}