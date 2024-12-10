package com.example.newsappmodern.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.example.newsappmodern.R
import com.example.newsappmodern.databinding.FragmentArticlePageBinding
import com.example.newsappmodern.models.Article
import com.example.newsappmodern.viewmodel.NewsViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar

class ArticleFragment: Fragment() {
    private lateinit var articlePageBinding: FragmentArticlePageBinding
    private lateinit var viewModel: NewsViewModel
    private val args: ArticleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        articlePageBinding = FragmentArticlePageBinding.inflate(layoutInflater)


        return articlePageBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as NewsActivity).viewModel

        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(articlePageBinding.toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val navController = findNavController()
        activity.supportActionBar?.title = getString(R.string.home)

        //Handle the back arrow click
        articlePageBinding.toolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }


        val article: Article = args.article

        Glide.with(this).
        load(article.imageUrl).
        placeholder(R.drawable.article_placeholder_image).into(articlePageBinding.sivArticleImage)

        articlePageBinding.cvArticleCollapsed.articleHeadline.text = article.title
        articlePageBinding.cvArticleCollapsed.articlePublishedDate.text = article.pubDate
        articlePageBinding.tvArticleDescription.text = article.description

        articlePageBinding.fab.setOnClickListener{
            articlePageBinding.fab.setImageResource(R.drawable.bookmark_filled)
            articlePageBinding.fab.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.scarletRed))
        }

        articlePageBinding.fab.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(view,"Article Saved",Snackbar.LENGTH_SHORT).show()
        }
    }

}