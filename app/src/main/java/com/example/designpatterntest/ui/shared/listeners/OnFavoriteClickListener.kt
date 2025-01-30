package com.example.designpatterntest.ui.shared.listeners

import com.example.designpatterntest.data.model.Product

interface OnFavoriteClickListener {
    fun onFavoriteClick(product: Product)
}