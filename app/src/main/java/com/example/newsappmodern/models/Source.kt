package com.example.newsappmodern.models

import android.os.Parcelable

import kotlinx.parcelize.Parcelize

@Parcelize
data class Source(
    val sourceId:String,
    val sourceName:String,
    val sourceUrl:String,
): Parcelable
