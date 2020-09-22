package com.androiddevs.shoppinglisttestingyt.repository

import androidx.lifecycle.LiveData
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingDao
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.androiddevs.shoppinglisttestingyt.data.remote.response.ImageResponse
import com.androiddevs.shoppinglisttestingyt.network.PixabayAPI
import com.androiddevs.shoppinglisttestingyt.utils.Resource
import retrofit2.HttpException
import javax.inject.Inject

class DefaultShoppingRepository
@Inject
constructor(
    private val shoppingDao: ShoppingDao,
    private val pixabayAPI: PixabayAPI
) : ShoppingRepository {

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.deleteShoppingItem(shoppingItem)
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return shoppingDao.observeAllShoppingItems()
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return shoppingDao.observeTotalPrice()
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {

        try {

            val response = pixabayAPI.searchForImage(imageQuery)

            return if (response.isSuccessful) {

                response.body()?.let { Resource.success(it) }
                    ?: Resource.error(msg = "Couldn't get images", data = null)

            } else {

                Resource.error(msg = "Bad Response", data = null)

            }

        } catch (ex: HttpException) {

            return Resource.error(
                msg = "Couldn't reach the server. Check your Internet connection", data = null
            )

        }

    }
}