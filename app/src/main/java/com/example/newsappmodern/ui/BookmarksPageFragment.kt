package com.example.newsappmodern.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsappmodern.R
import com.example.newsappmodern.adapters.NewsAdapterCollapsed
import com.example.newsappmodern.databinding.FragmentBookmarksPageBinding
import com.example.newsappmodern.databinding.FragmentHomePageBinding
import com.example.newsappmodern.viewmodel.NewsViewModel

class BookmarksPageFragment: Fragment() {

    private lateinit var viewModel: NewsViewModel
    private lateinit var bookmarksPageBinding: FragmentBookmarksPageBinding
    private lateinit var newsAdapter: NewsAdapterCollapsed


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        bookmarksPageBinding = FragmentBookmarksPageBinding.inflate(layoutInflater)

        return bookmarksPageBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as NewsActivity).viewModel

        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(bookmarksPageBinding.toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        activity.supportActionBar?.title = getString(R.string.bookmarks)

        setUpRecyclerView()

        newsAdapter.setOnItemClickListener { article ->
            val bundle = Bundle().apply {
                putParcelable("article",article)
            }

            findNavController().navigate(
                R.id.action_fragmentBookmarksPage_to_articleFragment,bundle
            )
        }

        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer { articles ->
            newsAdapter.differ.submitList(articles)
        })
    }

    private fun setUpRecyclerView(){
        newsAdapter = NewsAdapterCollapsed()
        bookmarksPageBinding.rvSavedNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}