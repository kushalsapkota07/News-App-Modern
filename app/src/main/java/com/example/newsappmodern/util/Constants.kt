package com.example.newsappmodern.util


import com.example.newsappmodern.R

class Constants {
    companion object{
        const val API_KEY = "pub_57534a151e9b364446a690336854c5aecf683"
        const val BASE_URL = "https://newsdata.io/api/"
        const val SEARCH_NEWS_TIME_DELAY = 500L
        const val QUERY_PAGE_SIZE = 30
        const val PREFERENCE_NAME = "NEWS_APP_PREF"
        const val PREF_IS_DARK_MODE = "PREF_IS_DARK_MODE"
        const val PREF_COUNTRY = "PREF_COUNTRY"
        const val PREF_COUNTRY_CODE = "PREF_COUNTRY_CODE"
        const val PREF_TEXT_SIZE = "PREF_TEXT_SIZE"
    }
}
enum class Category(val cat:String){
    BUSINESS("business"),
    SPORTS("sports"),
    SCIENCE("science"),
    EDUCATION("education"),
    ENTERTAINMENT("entertainment")
}


enum class TextSize(val size:Float){
    SMALL(12f), MEDIUM(16f), LARGE(20f)
}


enum class Countries(val country:String,val countryCode:String, val icon:Int) {
    World("World","wo",R.drawable.wo),
    Australia("Australia","au", R.drawable.au),
    UnitedStates("United States","us", R.drawable.us),
    UnitedKingdom("United Kingdom","uk", R.drawable.uk),
    Nepal("Nepal","np", R.drawable.np),
    India("India","in", R.drawable.`in`),
    Canada("Canada","ca", R.drawable.ca),
    Germany("Germany","de",R.drawable.de);

    companion object {
        fun indexOf(country: String): Int {
            return Countries.entries.toTypedArray().indexOf(Countries.valueOf(country))
        }
    }
}

fun Countries.checkClass(country: String):Boolean{
    return this.name == country
}


