package com.example.designpatterntest.favorites

import com.example.designpatterntest.model.Product

interface FavoritesView {
    fun getFavoriteProducts(products: List<Product>)
    fun showMessage(str: String)
}