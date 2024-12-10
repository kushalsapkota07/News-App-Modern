package com.example.newsappmodern.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsappmodern.R
import com.example.newsappmodern.databinding.ActivityNewsBinding
import com.example.newsappmodern.db.ArticleDatabase
import com.example.newsappmodern.repository.NewsRepository
import com.example.newsappmodern.repository.UserPreferencesRepository
import com.example.newsappmodern.viewmodel.NewsViewModel
import com.example.newsappmodern.viewmodel.NewsViewModelProviderFactory
import com.google.android.material.navigation.NavigationBarView


class NewsActivity : AppCompatActivity() {
    private lateinit var newsBinding: ActivityNewsBinding
    lateinit var viewModel: NewsViewModel
    private lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        newsBinding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(newsBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application,newsRepository)
        userPreferencesRepository = UserPreferencesRepository.getInstance(this)

        if(userPreferencesRepository.getDarkMode()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        viewModel = ViewModelProvider(this, viewModelProviderFactory)[NewsViewModel::class.java]

        val navHostFragment: NavHostFragment = supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        val navController = navHostFragment.findNavController()

        newsBinding.bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener{_,destination,_ ->
            when(destination.id){
                R.id.articleFragment -> newsBinding.bottomNavigationView.visibility = GONE
                else -> newsBinding.bottomNavigationView.visibility = VISIBLE
            }
        }
    }
}