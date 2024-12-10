package com.example.newsappmodern.db

import androidx.room.TypeConverter
import com.example.newsappmodern.models.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source?):String?{
        return source?.sourceName
    }

    @TypeConverter
    fun toSource(name: String?): Source? {
        return if (name.isNullOrEmpty()) {
            null
        } else {
            Source(
                sourceId = "",
                sourceName = name,
                sourceUrl = ""
            )
        }
    }
}