package com.example.designpatterntest.ui.products

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.designpatterntest.data.model.Product
import com.example.designpatterntest.data.repository.ProductsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductsViewModel(
    private val productsRepository: ProductsRepository
):ViewModel(){

    private val _viewState = MutableLiveData<ProductsViewState>()
    val viewState: LiveData<ProductsViewState> = _viewState

    private val _product = MutableLiveData<Product>()
    val product: LiveData<Product> = _product
    fun setProduct(product: Product){
        Log.d("wtf", "viewModelScope: ${product.title} ")
        _product.value = product
    }

    fun getProducts() {
        _viewState.value = ProductsViewState.Loading
        viewModelScope.launch(Dispatchers.IO){
            try {
                val productList = productsRepository.getProducts()
                Log.d("getProducts", "viewModel ${productList[0].title}")
                withContext(Dispatchers.Main){
                    _viewState.value = ProductsViewState.Success(productList.filter {
                        it.id !in listOf(6, 9, 19)
                    })
                }

            } catch (e: Exception) {
               withContext(Dispatchers.Main){
                   _viewState.value = ProductsViewState.Error(
                       type = ProductsViewState.ErrorType.LoadError,
                       message = "Failed to load products: ${e.message}"
                   )
               }
            }

        }
    }
    fun addProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = productsRepository.addProduct(product)
                withContext(Dispatchers.Main) {
                    if (result > 0) {
                        _viewState.value =
                            ProductsViewState.Message(
                                action = ProductsViewState.Action.ADDED_TO_FAVORITES,
                                message = "Product added to favorites"
                            ) // Emit success state
                    } else {
                        _viewState.value =
                            ProductsViewState.Error(
                                type = ProductsViewState.ErrorType.AddToFavoriteError,
                                message = "Failed to add product to favorites"
                            )
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _viewState.value =ProductsViewState.Error(
                        type = ProductsViewState.ErrorType.AddToFavoriteError,
                        message = "An Error Occurred ${e.message}"
                    )
                }
            }
        }
    }
     fun deleteProduct(id: Int) {
         viewModelScope.launch(Dispatchers.IO) {
             try {
                 val result = productsRepository.deleteProduct(id)
                 withContext(Dispatchers.Main) {
                     if (result > 0) {
                         _viewState.value =
                             ProductsViewState.Message(
                                 action = ProductsViewState.Action.REMOVED_FROM_FAVORITES,
                                 message = "Product removed from favorites"
                             )
                     } else {
                         _viewState.value = ProductsViewState.Error(
                             type = ProductsViewState.ErrorType.DeleteError,
                             message = "Failed to delete product from favorites"
                         )
                     }
                 }
             } catch (e: Exception) {
                 withContext(Dispatchers.Main) {
                     _viewState.value =ProductsViewState.Error(
                         type = ProductsViewState.ErrorType.DeleteError,
                         message = "An Error Occurred ${e.message}"
                     ) // Emit error state
                 }
             }
         }
     }
    fun toggleFavButton(product: Product){
        product.isFavorite = !product.isFavorite
    }
}