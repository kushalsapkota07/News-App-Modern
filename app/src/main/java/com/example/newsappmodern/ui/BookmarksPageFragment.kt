package com.example.newsappmodern.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.newsappmodern.databinding.FragmentBookmarksPageBinding
import com.example.newsappmodern.databinding.FragmentHomePageBinding

class BookmarksPageFragment: Fragment() {
    private lateinit var bookmarksPageBinding: FragmentBookmarksPageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bookmarksPageBinding = FragmentBookmarksPageBinding.inflate(layoutInflater)
        return bookmarksPageBinding.root
    }
}