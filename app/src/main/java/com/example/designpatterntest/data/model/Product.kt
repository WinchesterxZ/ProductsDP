package com.example.designpatterntest.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val category: String,
    val description: String,
    val discountPercentage: Double,
    val price: Double,
    val rating: Double,
    val stock: Int,
    val thumbnail: String,
    val title: String,
    val weight: Int,
    var isFavorite: Boolean = false
): Parcelable