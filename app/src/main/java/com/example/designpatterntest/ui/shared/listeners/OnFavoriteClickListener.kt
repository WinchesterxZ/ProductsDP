package com.example.designpatterntest.util

import com.example.designpatterntest.data.model.Product

interface OnFavoriteClickListener {
    fun onFavoriteClick(product: Product)
}