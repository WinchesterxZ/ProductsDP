package com.example.designpatterntest

import com.example.designpatterntest.model.Product

interface OnFavoriteClickListener {
    fun onFavoriteClick(product: Product)
}