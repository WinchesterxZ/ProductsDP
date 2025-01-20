package com.example.designpatterntest.util

import com.example.designpatterntest.model.Product

interface OnFavoriteClickListener {
    fun onFavoriteClick(product: Product)
}