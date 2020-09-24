package com.androiddevs.shoppinglisttestingyt.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.androiddevs.shoppinglisttestingyt.data.remote.response.ImageResponse
import com.androiddevs.shoppinglisttestingyt.repository.ShoppingRepository
import com.androiddevs.shoppinglisttestingyt.utils.Constants
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

        val hasEmptyInputs = name.isEmpty() || amountString.isEmpty() || priceString.isEmpty()
        if (hasEmptyInputs) {
            val errorResource = Resource.error("The fields must not be empty", null)
            insertShoppingItemStatus.postValue(Event(errorResource))
            return
        }

        if (name.length > Constants.MAX_NAME_LENGTH) {
            val errorMessage =
                "The name of the item must not exceed ${Constants.MAX_NAME_LENGTH} characters"
            val errorResource = Resource.error(errorMessage, null)
            insertShoppingItemStatus.postValue(Event(errorResource))
            return
        }

        if (priceString.length > Constants.MAX_PRICE_LENGTH) {
            val errorMessage =
                "The price of the item must not exceed ${Constants.MAX_PRICE_LENGTH} characters"
            val errorResource = Resource.error(errorMessage, null)
            insertShoppingItemStatus.postValue(Event(errorResource))
            return
        }

        val amount = try {

            amountString.toInt()

        } catch (ex: Exception) {

            val errorMessage = "Please enter a valid amount"
            val errorResource = Resource.error(errorMessage, null)
            insertShoppingItemStatus.postValue(Event(errorResource))
            return

        }

        val imageURL = currentImageURL.value ?: ""
        val shoppingItem = ShoppingItem(name, amount, priceString.toFloat(), imageURL, 0)

        insertShoppingItemIntoDb(shoppingItem)
        setCurrentImageURL("") // Clearing the image url since we want to show blank preview now

        val successResource = Resource.success(shoppingItem)
        insertShoppingItemStatus.postValue(Event(successResource))
    }

    fun searchForImage(imageQuery: String) {

        if (imageQuery.isEmpty()) return

        images.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.searchForImage(imageQuery)
            images.value = Event(response)
        }

    }

}