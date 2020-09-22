package com.androiddevs.shoppinglisttestingyt.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.androiddevs.shoppinglisttestingyt.data.remote.response.ImageResponse
import com.androiddevs.shoppinglisttestingyt.repository.ShoppingRepository
import com.androiddevs.shoppinglisttestingyt.utils.Event
import com.androiddevs.shoppinglisttestingyt.utils.Resource
import kotlinx.coroutines.launch

class ShoppingViewModel @ViewModelInject constructor(
    private val repository: ShoppingRepository
) : ViewModel() {

    val shoppingItems = repository.observeAllShoppingItems()
    val totalPrice = repository.observeTotalPrice()

    private val images = MutableLiveData<Event<Resource<ImageResponse>>>()

    fun getImages() = images as LiveData<Event<Resource<ImageResponse>>>

    private val currentImageURL = MutableLiveData<String>()

    fun getCurrentImageURL() = currentImageURL as LiveData<String>

    private val insertShoppingItemStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()

    fun getShoppingItemStatus() = insertShoppingItemStatus
            as LiveData<Event<Resource<ShoppingItem>>>

    fun setCurrentImageURL(url: String) {
        currentImageURL.postValue(url)
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.deleteShoppingItem(shoppingItem)
    }

    fun insertShoppingItemIntoDb(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.insertShoppingItem(shoppingItem)
    }

    fun insertShoppingItem(name: String, amountString: String, priceString: String) {

    }

    fun searchForImage(imageQuery: String) {

    }

}