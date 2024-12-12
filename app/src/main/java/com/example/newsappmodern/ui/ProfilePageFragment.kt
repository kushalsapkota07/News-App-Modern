package com.example.newsappmodern.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.newsappmodern.R
import com.example.newsappmodern.adapters.CountryAdapter
import com.example.newsappmodern.databinding.FragmentProfilePageBinding
import com.example.newsappmodern.databinding.ItemCountryBinding
import com.example.newsappmodern.repository.UserPreferencesRepository
import com.example.newsappmodern.util.Countries
import com.example.newsappmodern.viewmodel.NewsViewModel
import kotlin.properties.Delegates

class ProfilePageFragment: Fragment() {
    private lateinit var profilePageBinding: FragmentProfilePageBinding
    private lateinit var viewModel: NewsViewModel
    private lateinit var countryAdapter: CountryAdapter
    private lateinit var userPreferencesRepository: UserPreferencesRepository
    private var country by Delegates.notNull<String>()
    private var isDarkMode by Delegates.notNull<Boolean>()
    private var textSize by Delegates.notNull<Float>()
    private var countryIndex by Delegates.notNull<Int>()
    private var countryCode by Delegates.notNull<String>()
    private var initialSelection= true


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profilePageBinding = FragmentProfilePageBinding.inflate(layoutInflater)

        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(profilePageBinding.toolbar)
        activity.supportActionBar?.title = getString(R.string.profile)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        viewModel = (activity as NewsActivity).viewModel
        userPreferencesRepository = UserPreferencesRepository.getInstance(requireContext())

        return profilePageBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        country = userPreferencesRepository.getCountry()
        isDarkMode = userPreferencesRepository.getDarkMode()
        textSize = userPreferencesRepository.getTextSize()
        countryIndex = Countries.indexOf(country.filter { !it.isWhitespace() })
        countryCode = "wo"
        var selectedCountry = ""
        var selectedCountryCode = ""
        var selectedCountryIndex:Int = countryIndex



        countryAdapter = CountryAdapter(requireContext())

        profilePageBinding.sCountries.adapter = countryAdapter

        profilePageBinding.sCountries.setSelection(selectedCountryIndex + 1,false)


        profilePageBinding.darkModeSwitchView.darkModeSwitch.isChecked = isDarkMode

        viewModel.isConfirmed.observe(viewLifecycleOwner, Observer {
            initialSelection=true
            if(viewModel.isConfirmed.value==true){
                Toast.makeText(requireContext(),"Restart Clicked",Toast.LENGTH_SHORT).show()
                userPreferencesRepository.setCountryCode(selectedCountryCode)
                userPreferencesRepository.setCountry(selectedCountry)
                println("Selected: $selectedCountry $selectedCountryCode")

                Thread.sleep(2000)
                restartActivity(requireContext())
            } else{

                profilePageBinding.sCountries.setSelection(countryIndex+1)

            }

        })


        profilePageBinding.sCountries.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                // Ignore the first call due to initialization

                if (!initialSelection) {
                    val showPopUp = ChangeCountryFragment()
                    showPopUp.show((activity as AppCompatActivity).supportFragmentManager, "showPopUp")
                }
                //Handle the selection
                val selection:Countries = p0?.getItemAtPosition(p2) as Countries
                selectedCountry = selection.country
                selectedCountryCode = selection.countryCode
                selectedCountryIndex = p2

                //Reset the flag after first interaction
                initialSelection = false
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
        profilePageBinding.darkModeSwitchView.darkModeSwitch.setOnClickListener {
            if(profilePageBinding.darkModeSwitchView.darkModeSwitch.isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                isDarkMode = true
                userPreferencesRepository.setDarkMode(isDarkMode)


            } else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                isDarkMode=false
                userPreferencesRepository.setDarkMode(isDarkMode)

            }
        }

        profilePageBinding.customizeTextSize.setOnButtonClickListener(object : CustomTextSizeSelector.OnButtonClickListener{
            override fun onButtonClicked(buttonId: Int) {
                when (buttonId){
                    1 -> textSize = profilePageBinding.customizeTextSize.textSize
                    2 -> textSize = profilePageBinding.customizeTextSize.textSize
                    3 -> textSize = profilePageBinding.customizeTextSize.textSize

                }
                userPreferencesRepository.setTextSize(textSize)

            }
        })
    }

    private fun restartActivity(context: Context){
        val packageManager = context.packageManager
        val intent = packageManager.getLaunchIntentForPackage(context.packageName)
        val componentName = intent?.component
        val mainIntent = Intent.makeRestartActivityTask(componentName)
        context.startActivity(mainIntent)
        Runtime.getRuntime().exit(0)
    }
}