package com.example.designpatterntest.allProducts

import com.example.designpatterntest.model.Product

interface AllProductsView {
    fun showProducts(products: List<Product>)
    fun showMessage(str: String)
}