package com.example.designpatterntest.di

import com.example.designpatterntest.data.db.ProductDatabase
import com.example.designpatterntest.data.network.ApiService
import com.example.designpatterntest.data.network.RetrofitHelper
import com.example.designpatterntest.data.repository.ProductsRepository
import com.example.designpatterntest.ui.favorites.FavoriteProductsViewModel
import com.example.designpatterntest.ui.products.ProductsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // Provide the Room Database instance
    single { ProductDatabase.getInstance(get()) }

    // Provide the ProductDao instance
    single { get<ProductDatabase>().productDao() }

    // Provide Retrofit ApiService from RetrofitHelper
    single { RetrofitHelper.retrofitService }

    // Provide Repository (inject ApiService and ProductDao)
    single { ProductsRepository(get(), get()) }

    // Provide ViewModel (inject ProductsRepository)
    viewModel { ProductsViewModel(get())}
    viewModel { FavoriteProductsViewModel(get()) }
}
