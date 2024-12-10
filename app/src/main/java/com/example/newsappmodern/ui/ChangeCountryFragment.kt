package com.example.newsappmodern.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.newsappmodern.databinding.FragmentConfirmCountryChangeBinding
import com.example.newsappmodern.viewmodel.NewsViewModel

class ChangeCountryFragment(
): DialogFragment() {
    private lateinit var confirmCountryChangeBinding: FragmentConfirmCountryChangeBinding
    private lateinit var viewModel: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        confirmCountryChangeBinding = FragmentConfirmCountryChangeBinding.inflate(layoutInflater)
        viewModel = (activity as NewsActivity).viewModel
        return confirmCountryChangeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        confirmCountryChangeBinding.btnCancel.setOnClickListener {
            viewModel.confirm(false)
            dismiss()
        }
        confirmCountryChangeBinding.btnRestart.setOnClickListener {
            viewModel.confirm(true)
            dismiss()
        }

    }
}