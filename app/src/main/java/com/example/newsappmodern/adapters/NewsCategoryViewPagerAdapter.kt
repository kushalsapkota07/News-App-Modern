package com.example.newsappmodern.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.newsappmodern.ui.CurrentNewsCategoryFragment

class NewsCategoryViewPagerAdapter(
    fragment: Fragment,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(
    fragmentManager, lifecycle
) {
    override fun getItemCount(): Int {
        return Companion.numberOfTabs
    }

    override fun createFragment(position: Int): Fragment {
       val fragment = when(position){
           0 -> CurrentNewsCategoryFragment.newInstance("business")
           1 -> CurrentNewsCategoryFragment.newInstance("education")
           2 -> CurrentNewsCategoryFragment.newInstance("sports")
           3 -> CurrentNewsCategoryFragment.newInstance("science")
           4 -> CurrentNewsCategoryFragment.newInstance("entertainment")
           else -> CurrentNewsCategoryFragment.newInstance("sports")
       }
        return fragment
    }

    companion object {
        var numberOfTabs: Int = 5
    }

    fun ViewPager2.findCurrentFragment(fragmentManager: FragmentManager): Fragment? {
        return fragmentManager.findFragmentByTag("f$currentItem")
    }
}