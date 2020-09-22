package com.androiddevs.shoppinglisttestingyt.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.androiddevs.shoppinglisttestingyt.R

class ImagePickFragment : Fragment(R.layout.fragment_image_pick) {

    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[ShoppingViewModel::class.java]
    }

}