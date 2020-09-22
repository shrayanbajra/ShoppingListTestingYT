package com.androiddevs.shoppinglisttestingyt.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.androiddevs.shoppinglisttestingyt.R

class AddShoppingItemFragment : Fragment(R.layout.fragment_add_shopping_item) {

    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[ShoppingViewModel::class.java]
    }

}