package com.example.designpatterntest.data.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: String?): List<String> {
        return value?.let {
            gson.fromJson(it, object : TypeToken<List<String>>() {}.type)
        } ?: emptyList()
    }

    @TypeConverter
    fun toStringList(list: List<String>?): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromReviewList(value: String?): List<Review> {
        return value?.let {
            gson.fromJson(it, object : TypeToken<List<Review>>() {}.type)
        } ?: emptyList()
    }

    @TypeConverter
    fun toReviewList(list: List<Review>?): String {
        return gson.toJson(list)
    }
}
