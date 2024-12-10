package com.example.newsappmodern.models

import com.example.newsappmodern.util.Countries
import com.example.newsappmodern.util.TextSize

class UserPrefs(
        val country:String = Countries.World.country,
        val countryCode:String = Countries.World.countryCode,
        val isDarkModeOn:Boolean = true,
        val textSize:Float = TextSize.MEDIUM.size
){
}