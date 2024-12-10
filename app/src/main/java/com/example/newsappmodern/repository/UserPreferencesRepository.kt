package com.example.newsappmodern.repository

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.example.newsappmodern.util.Constants.Companion.PREF_COUNTRY
import com.example.newsappmodern.util.Constants.Companion.PREF_COUNTRY_CODE
import com.example.newsappmodern.util.Constants.Companion.PREF_IS_DARK_MODE
import com.example.newsappmodern.util.Constants.Companion.PREF_TEXT_SIZE


class UserPreferencesRepository(context: Context) {
     val pref: SharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    private val defaultPrefs = "{\n" +
            "  \"country\": \"World\",\n" +
            "  \"isDarkModeOn\": true,\n" +
            "  \"textSize\": 16\n" +
            "}"
    private val editor:Editor = pref.edit()

    private fun String.put(textSize:Float){
        editor.putFloat(this,textSize)
        editor.apply()
    }
    private fun String.put(country:String){
        editor.putString(this,country)
        editor.apply()
    }
    private fun String.put(isDarkMode: Boolean){
        editor.putBoolean(this,isDarkMode)
        editor.apply()
    }

    private fun String.getFloat() = pref.getFloat(this,16f)
    private fun String.getString() = pref.getString(this,"World")!!
    private fun String.getBoolean() = pref.getBoolean(this,true)

    fun setDarkMode(isDarkMode: Boolean){
        PREF_IS_DARK_MODE.put(isDarkMode)
    }

    fun setCountry(country: String){
        PREF_COUNTRY.put(country)
    }

    fun setTextSize(textSize: Float){
        PREF_TEXT_SIZE.put(textSize)
    }
    fun setCountryCode(countryCode:String){
        PREF_COUNTRY_CODE.put(countryCode)
    }

    fun getDarkMode() = PREF_IS_DARK_MODE.getBoolean()
    fun getCountry() = PREF_COUNTRY.getString()
    fun getTextSize() = PREF_TEXT_SIZE.getFloat()
    fun getCountryCode() = PREF_COUNTRY_CODE.getString()

    fun clearData(){
        editor.clear()
        editor.commit()
    }

    companion object{
        @Volatile
        private var instance:UserPreferencesRepository?=null

        fun getInstance(context: Context): UserPreferencesRepository{
            return instance ?: synchronized(this){
                instance ?: UserPreferencesRepository(context.applicationContext)
                    .also { instance = it }
            }
        }
    }
}