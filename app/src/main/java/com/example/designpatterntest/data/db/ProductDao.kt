package com.example.designpatterntest.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.designpatterntest.data.model.Product

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    suspend fun getAllProducts(): List<Product>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProducts(products: List<Product>):List<Long>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product): Long
    @Query("SELECT * FROM products WHERE isFavorite = 1")
    suspend fun getFavoriteProducts(): List<Product>
    @Query("DELETE FROM products WHERE id = :id")
    suspend fun deleteProduct(id: Int): Int

}