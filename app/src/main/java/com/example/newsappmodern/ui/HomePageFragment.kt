package com.example.newsappmodern.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.newsappmodern.R
import com.example.newsappmodern.adapters.NewsAdapterCardView
import com.example.newsappmodern.adapters.NewsCategoryViewPagerAdapter
import com.example.newsappmodern.databinding.FragmentHomePageBinding
import com.example.newsappmodern.models.NewsResponse
import com.example.newsappmodern.repository.UserPreferencesRepository
import com.example.newsappmodern.util.Constants.Companion.QUERY_PAGE_SIZE
import com.example.newsappmodern.util.Resource
import com.example.newsappmodern.viewmodel.NewsViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomePageFragment: Fragment() {
    private lateinit var homePageBinding: FragmentHomePageBinding
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapterCardView: NewsAdapterCardView
    private lateinit var newsCategoryViewPagerAdapter: NewsCategoryViewPagerAdapter
    private lateinit var userPreferencesRepository: UserPreferencesRepository


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homePageBinding = FragmentHomePageBinding.inflate(layoutInflater)
        userPreferencesRepository = UserPreferencesRepository.getInstance(requireContext())

        return homePageBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as NewsActivity).viewModel

        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(homePageBinding.toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        activity.supportActionBar?.title = getString(R.string.home)

        setUpHorizontalRecyclerView()
        setUpCurrentTab()

        newsAdapterCardView.setOnItemClickListener { article ->
            val bundle = Bundle().apply {
                putParcelable("article",article)
            }

            findNavController().navigate(
                R.id.action_fragmentHomePage_to_articleFragment,bundle
            )
        }

        viewModel.getTopTrendingNews(
            userPreferencesRepository.getCountryCode(),
            "top",
            null
        )

        //Observing data - horizontal recyclerview
        viewModel.topTrendingNews.observe( viewLifecycleOwner, Observer { response ->
            when (response){
                is Resource.Success -> {
                    hideProgressBarHorizontalRV()
                    response.data?.let { newsResponse: NewsResponse ->
                        newsAdapterCardView.differ.submitList(newsResponse.results.toList())

                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE +2
                        isLastPage = viewModel.topTrendingPage == totalPages
                        if(isLastPage){
                            homePageBinding.rvTopTrendingNews.setPadding(0,0,0,0)
                        }
                    }

                }
                is Resource.Error -> {
                    hideProgressBarHorizontalRV()
                    response.message?.let { message ->
                        homePageBinding.tvErrorMessageHorizontalRV.visibility = View.VISIBLE
                        homePageBinding.tvErrorMessageHorizontalRV.text = message
                    }
                }
                is Resource.Loading -> {
                    showProgressBarHorizontalRV()
                }
            }

        })
    }

    private fun setUpCurrentTab(){

        homePageBinding.tlNewsCategory.apply {
            addTab(newTab().setText("Business"))
            addTab(newTab().setText("Education"))
            addTab(newTab().setText("Sports"))
            addTab(newTab().setText("Science"))
            addTab(newTab().setText("Entertainment"))

        }
        newsCategoryViewPagerAdapter = NewsCategoryViewPagerAdapter(this,childFragmentManager, lifecycle)
        homePageBinding.viewpager2.adapter = newsCategoryViewPagerAdapter
        homePageBinding.viewpager2.offscreenPageLimit = 1

        homePageBinding.tlNewsCategory.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?)
            {
                tab?.let {
                    viewModel.selectedCategory.value = tab.text.toString()
                    homePageBinding.viewpager2.currentItem = tab.position
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        homePageBinding.viewpager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                    homePageBinding.tlNewsCategory.selectTab(homePageBinding.tlNewsCategory.getTabAt(position))
            }
        })

    }

    private fun setUpHorizontalRecyclerView(){
        newsAdapterCardView = NewsAdapterCardView()
        homePageBinding.rvTopTrendingNews.apply {
            adapter = newsAdapterCardView
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            addOnScrollListener(scrollListener)

        }
    }

    private fun hideProgressBarHorizontalRV(){
        homePageBinding.progressBarCardView.visibility = View.INVISIBLE
        homePageBinding.tvErrorMessageHorizontalRV.visibility = View.INVISIBLE
    }


    private fun showProgressBarHorizontalRV(){
        homePageBinding.progressBarCardView.visibility = View.VISIBLE
        homePageBinding.tvErrorMessageHorizontalRV.visibility = View.VISIBLE
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    //On Scroll Listener for Recycler view
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            //detecting if we have scrolled to the bottom
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val totalVisibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + totalVisibleItemCount >= totalItemCount

            //checking if the first item is visible or not
            val isNotAtBeginning = firstVisibleItemPosition >= 0

            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE

            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                println("Inside should paginate")
                viewModel.topTrendingNewsPage?.let {
                    CoroutineScope(Dispatchers.IO).launch {
                        viewModel.getTopTrendingNews(
                            userPreferencesRepository.getCountryCode(),
                            "top",
                            it
                        )
                    }

                }
                println("${viewModel.topTrendingNewsPage} is the page number")

                isScrolling = false
            }
        }
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            //detecting if we are currently scrolling
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }
    }
}