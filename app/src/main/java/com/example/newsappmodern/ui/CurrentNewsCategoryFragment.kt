package com.example.newsappmodern.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsappmodern.R
import com.example.newsappmodern.adapters.NewsAdapterCollapsed
import com.example.newsappmodern.databinding.CurrentNewsCategoryItemBinding
import com.example.newsappmodern.models.NewsResponse
import com.example.newsappmodern.repository.UserPreferencesRepository
import com.example.newsappmodern.util.Resource
import com.example.newsappmodern.viewmodel.NewsViewModel


private const val DATA = "category"
class CurrentNewsCategoryFragment(

): Fragment() {

    private lateinit var currentNewsCategoryItemBinding: CurrentNewsCategoryItemBinding
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapterCollapsed: NewsAdapterCollapsed
    private var value:String = ""
    private lateinit var userPreferencesRepository: UserPreferencesRepository
    private var prefListener: SharedPreferences.OnSharedPreferenceChangeListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        value = arguments?.getString(DATA).toString()
        println("Current Tab Text: $value")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        currentNewsCategoryItemBinding = CurrentNewsCategoryItemBinding.inflate(inflater)
        userPreferencesRepository = UserPreferencesRepository.getInstance(requireContext())
        return currentNewsCategoryItemBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel


        setUpVerticalRecyclerView()
        getCurrentNews()
        newsAdapterCollapsed.setOnItemClickListener { article ->
            val bundle = Bundle().apply {
                putParcelable("article",article)
            }
            findNavController().navigate(
                R.id.action_fragmentHomePage_to_articleFragment,
                bundle
                )

        }

    }

    private fun getCurrentNews(){
        viewModel.getCurrentNews(userPreferencesRepository.getCountryCode(),value).observe(viewLifecycleOwner, Observer { response ->
            when (response){
                is Resource.Success -> {
                    hideProgressBarVerticalRV()
                    response.data?.let { newsResponse: NewsResponse ->
                        newsAdapterCollapsed.differ
                        newsAdapterCollapsed.differ.submitList(newsResponse.results.toList())
                    }
                }
                is Resource.Error -> {
                    hideProgressBarVerticalRV()
                    response.message?.let { message ->
                        currentNewsCategoryItemBinding.tvErrorMessageVerticalRV.visibility = View.VISIBLE
                        currentNewsCategoryItemBinding.tvErrorMessageVerticalRV.text = message
                    }
                }
                is Resource.Loading -> {
                    showProgressBarVerticalRV()
                }
            }
        })
    }

    private fun setUpVerticalRecyclerView(){
                newsAdapterCollapsed = NewsAdapterCollapsed()
        currentNewsCategoryItemBinding.rvCurrentNewsCategory.apply {
            adapter = newsAdapterCollapsed
            layoutManager = LinearLayoutManager(activity)
        }
    }

        private fun hideProgressBarVerticalRV(){
        currentNewsCategoryItemBinding.progressBarCollapsed.visibility = View.INVISIBLE
        currentNewsCategoryItemBinding.tvErrorMessageVerticalRV.visibility = View.INVISIBLE
    }

    private fun showProgressBarVerticalRV(){
        currentNewsCategoryItemBinding.progressBarCollapsed.visibility = View.VISIBLE
        currentNewsCategoryItemBinding.tvErrorMessageVerticalRV.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        userPreferencesRepository.pref.registerOnSharedPreferenceChangeListener(prefListener)
    }

    override fun onPause() {
        super.onPause()
        userPreferencesRepository.pref.unregisterOnSharedPreferenceChangeListener(prefListener)
    }

    companion object {
        @JvmStatic
        fun newInstance(data: String) =
            CurrentNewsCategoryFragment().apply {
                arguments = Bundle().apply {
                    putString(DATA, data)
                }
            }
    }
}