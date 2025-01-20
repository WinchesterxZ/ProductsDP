package com.example.designpatterntest.products

import com.example.designpatterntest.model.Product

interface ProductsView {
    fun showProducts(products: List<Product>)
    fun showMessage(str: String)
}