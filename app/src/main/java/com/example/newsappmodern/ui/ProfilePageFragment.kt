package com.example.newsappmodern.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.newsappmodern.databinding.FragmentHomePageBinding
import com.example.newsappmodern.databinding.FragmentProfilePageBinding

class ProfilePageFragment: Fragment() {
    private lateinit var profilePageBinding: FragmentProfilePageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profilePageBinding = FragmentProfilePageBinding.inflate(layoutInflater)
        return profilePageBinding.root
    }
}