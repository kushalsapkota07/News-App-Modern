package com.example.newsappmodern.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsappmodern.adapters.NewsAdapter
import com.example.newsappmodern.databinding.FragmentHomePageBinding
import com.example.newsappmodern.models.NewsResponse
import com.example.newsappmodern.util.Resource
import com.example.newsappmodern.viewmodel.NewsViewModel

class HomePageFragment: Fragment() {
    private lateinit var homePageBinding: FragmentHomePageBinding
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private val TAG = "HomePageFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homePageBinding = FragmentHomePageBinding.inflate(layoutInflater)
        return homePageBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as NewsActivity).viewModel
        setUpRecyclerView()

        newsAdapter.setOnItemClickListener {
            Toast.makeText(requireContext(), "Item Clicked",Toast.LENGTH_SHORT).show()
        }

        //Observe the changes in the live data
        viewModel.latestNews.observe( viewLifecycleOwner, Observer { response ->
            when (response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse: NewsResponse ->
                        newsAdapter.differ.submitList(newsResponse.results.toList())
                    }

                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        homePageBinding.tvErrorMessage.visibility = View.VISIBLE
                        homePageBinding.tvErrorMessage.text = message
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }

        })
    }

    private fun setUpRecyclerView(){
        newsAdapter = NewsAdapter()
        homePageBinding.rvLatestNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun hideProgressBar(){
        homePageBinding.progressBar.visibility = View.INVISIBLE
        homePageBinding.tvErrorMessage.visibility = View.INVISIBLE
    }

    private fun showProgressBar(){
        homePageBinding.progressBar.visibility = View.VISIBLE
        homePageBinding.tvErrorMessage.visibility = View.VISIBLE
    }
}