package com.example.designpatterntest.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val availabilityStatus: String,
    val category: String,
    val description: String,
    val discountPercentage: Double,
    val images: List<String>,
    val price: Double,
    val rating: Double,
    val reviews: List<Review>,
    val stock: Int,
    val thumbnail: String,
    val title: String,
    var isFavorite: Boolean = false
)