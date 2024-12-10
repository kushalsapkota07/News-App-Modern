package com.example.newsappmodern.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.content.res.AppCompatResources
import com.example.newsappmodern.R
import com.example.newsappmodern.databinding.CustomPersonalizeViewBinding
import com.example.newsappmodern.databinding.HeaderCountryBinding
import com.example.newsappmodern.databinding.ItemCountryBinding
import com.example.newsappmodern.ui.CustomPersonalizeFeed
import com.example.newsappmodern.util.Countries
import java.util.Locale

class CountryAdapter(
    context: Context
) : ArrayAdapter<Countries>(context, 0, Countries.entries.toTypedArray()) {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    val numberOfItems = Countries.entries.toTypedArray().size

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ItemCountryBinding = if(convertView==null){
            ItemCountryBinding.inflate(layoutInflater, parent, false)
        } else{
            ItemCountryBinding.bind(convertView)
        }
        
        getItem(position)?.let { country ->
            setItemForCountry(binding, country)
        }
        
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return when{
            position==0 -> {
                val headerBinding = HeaderCountryBinding.inflate(layoutInflater, parent, false)
                headerBinding.root.background = AppCompatResources.getDrawable(context,R.drawable.spinner_item_background)
                headerBinding.root.setOnClickListener {
                    val root = parent.rootView
                    root.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK))
                    root.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK))
                }
                headerBinding.root
            }
            else -> {

                val dropDownBinding = ItemCountryBinding.inflate(LayoutInflater.from(context), parent, false)

                getItem(position)?.let { country ->

                    dropDownBinding.ivOptionItem.setImageResource(country.icon)
                    dropDownBinding.ivOptionItemExpand.setImageResource(R.drawable.gray_outline)
                    val countryName =
                        if (country.countryCode == "wo"){
                            "World"
                        } else {
                            Locale("",country.countryCode).displayCountry
                        }
                    dropDownBinding.tvOptionTitle.text = countryName

                    if(position==count-1){
                        dropDownBinding.root.background = AppCompatResources.getDrawable(context, R.drawable.item_country_last_background)
                    } else{
                        dropDownBinding.root.background = AppCompatResources.getDrawable(context, R.drawable.item_country_background)
                    }
                }

                dropDownBinding.root
            }

        }
    }

    override fun getItem(position: Int): Countries? {
        return if (position==0) null else super.getItem(position-1)
    }

    override fun getCount(): Int {
        return numberOfItems + 1 // Add one for the header
    }

    private fun setItemForCountry(binding: ItemCountryBinding, country: Countries) {

        val countryName =
        if (country.countryCode == "wo"){
            "World"
        } else {
            Locale("",country.countryCode).displayCountry
        }

        binding.ivOptionItem.setImageResource(country.icon)
        binding.tvOptionTitle.text = countryName
    }
}