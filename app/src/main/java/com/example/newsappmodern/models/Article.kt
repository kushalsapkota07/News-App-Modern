package com.example.newsappmodern.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    val articleId:String,
    val title:String,
    val link: String,
    val keywords: MutableList<String>,
    val creator:MutableList<String>,
    val description: String,
    val content:String,
    val pubDate:String,
    val pubDateTZ:String,
    @SerializedName("image_url") val imageUrl:String,
    val source: Source,
    val language:String,
    val country:MutableList<String>,
    val category: MutableList<String>,
    ) : Parcelable{

    }
