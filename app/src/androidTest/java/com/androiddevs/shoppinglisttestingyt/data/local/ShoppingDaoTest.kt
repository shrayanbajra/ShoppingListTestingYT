package com.androiddevs.shoppinglisttestingyt.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ShoppingDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ShoppingItemDatabase
    private lateinit var shoppingDao: ShoppingDao

    @Before
    fun setUp() {

        database = Room
            .inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(), ShoppingItemDatabase::class.java
            )
            .allowMainThreadQueries()
            .build()

        shoppingDao = database.shoppingDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertShoppingItem() = runBlockingTest {

        val shoppingItem = ShoppingItem("shirt", 1, 1f, "url", 1)
        shoppingDao.insertShoppingItem(shoppingItem)

        val allShoppingItems = shoppingDao.observeAllShoppingItems().getOrAwaitValue()

        assertThat(allShoppingItems).contains(shoppingItem)

    }

    @Test
    fun deleteShoppingItem() = runBlockingTest {

        val shoppingItem = ShoppingItem("shirt", 1, 1f, "url", 1)
        shoppingDao.insertShoppingItem(shoppingItem)

        shoppingDao.deleteShoppingItem(shoppingItem)

        val allShoppingItems = shoppingDao.observeAllShoppingItems().getOrAwaitValue()

        assertThat(allShoppingItems).doesNotContain(shoppingItem)

    }

    @Test
    fun observePriceSum() = runBlockingTest {

        val shoppingItem1 = ShoppingItem("shirt", 1, 1f, "url", 1)
        val shoppingItem2 = ShoppingItem("jeans", 2, 1f, "url", 2)
        shoppingDao.insertShoppingItem(shoppingItem1)
        shoppingDao.insertShoppingItem(shoppingItem2)

        val expectedSum = shoppingDao.observeTotalPrice().getOrAwaitValue()

        val shoppingItem1Sum = (shoppingItem1.amount * shoppingItem1.price)
        val shoppingItem2Sum = (shoppingItem2.amount * shoppingItem2.price)
        val actualSum = shoppingItem1Sum + shoppingItem2Sum

        assertThat(expectedSum).isEqualTo(actualSum)

    }

}