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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsappmodern.R
import com.example.newsappmodern.adapters.NewsAdapterCollapsed
import com.example.newsappmodern.databinding.FragmentBookmarksPageBinding
import com.example.newsappmodern.databinding.FragmentHomePageBinding
import com.example.newsappmodern.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar

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

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // viewHolder.adapterPosition gives the position of the item that is swiped
                val position = viewHolder.adapterPosition
                //newsAdapter.differ.currentList[position] gives the article at the position
                val article = newsAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(view, "Successfully deleted article", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        viewModel.saveArticle(article)
                    }
                    show()
                }
            }

        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(bookmarksPageBinding.rvSavedNews)

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