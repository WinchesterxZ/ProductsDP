package com.example.designpatterntest.data.network

import com.example.designpatterntest.data.model.ProductResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("products")
    suspend fun getProducts(): Response<ProductResponse>
}