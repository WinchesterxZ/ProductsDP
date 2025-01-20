package com.example.designpatterntest.api

import com.example.designpatterntest.model.ProductResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("products")
    suspend fun getProducts(): Response<ProductResponse>
}