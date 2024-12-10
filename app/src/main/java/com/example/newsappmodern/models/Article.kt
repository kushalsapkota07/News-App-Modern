package com.example.newsappmodern.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "articles"
)
@Parcelize
data class Article(
    @PrimaryKey(autoGenerate = true)
    val articleId:Int,
    val title:String?,
    val link: String?,
    val description: String?,
    val content:String,
    val pubDate:String,
    val pubDateTZ:String,
    @SerializedName("image_url") val imageUrl:String?,
    val source: Source?,
    val language:String?,
    ) : Parcelable {

    override fun hashCode(): Int {
        var result = articleId.hashCode()
        if(!link.isNullOrEmpty()){
            result = 31 * result + link.hashCode()
        }
        return result
    }

    }
