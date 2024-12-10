package com.example.newsappmodern.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.newsappmodern.R
import com.example.newsappmodern.adapters.NewsAdapterCardView
import com.example.newsappmodern.adapters.NewsCategoryViewPagerAdapter
import com.example.newsappmodern.databinding.FragmentHomePageBinding
import com.example.newsappmodern.models.NewsResponse
import com.example.newsappmodern.repository.UserPreferencesRepository
import com.example.newsappmodern.util.Resource
import com.example.newsappmodern.viewmodel.NewsViewModel
import com.google.android.material.tabs.TabLayout


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
            "top"
        )

        //Observing data - horizontal recyclerview
        viewModel.topTrendingNews.observe( viewLifecycleOwner, Observer { response ->
            when (response){
                is Resource.Success -> {
                    hideProgressBarHorizontalRV()
                    response.data?.let { newsResponse: NewsResponse ->
                        newsAdapterCardView.differ.submitList(newsResponse.results.toList())
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
}