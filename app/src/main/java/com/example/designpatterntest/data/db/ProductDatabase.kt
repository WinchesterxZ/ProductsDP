package com.example.designpatterntest.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.designpatterntest.data.model.Converters
import com.example.designpatterntest.data.model.Product

@Database(entities = [Product::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ProductDatabase: RoomDatabase() {
    abstract fun productDao(): ProductDao
    companion object {
        private const val DATABASE_NAME = "product_db"

        @Volatile
        private var INSTANCE: ProductDatabase? = null
        fun getInstance(context: Context): ProductDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context = context,
                    klass = ProductDatabase::class.java,
                    name = DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}