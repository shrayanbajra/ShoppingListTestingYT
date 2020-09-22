package com.androiddevs.shoppinglisttestingyt.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValueTest
import com.androiddevs.shoppinglisttestingyt.repository.FakeShoppingRepository
import com.androiddevs.shoppinglisttestingyt.utils.Constants
import com.androiddevs.shoppinglisttestingyt.utils.Status
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShoppingViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ShoppingViewModel

    @Before
    fun setup() {
        viewModel = ShoppingViewModel(FakeShoppingRepository())
    }

    @Test
    fun `insert shopping item with one empty field, returns error`() {

        viewModel.insertShoppingItem("name", "", "24.50")

        val value = viewModel.getShoppingItemStatus().getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)

    }

    @Test
    fun `insert shopping item with too long name, returns error`() {

        val invalidName = buildString {
            val maxLength = Constants.MAX_NAME_LENGTH
            for (i in 1..maxLength + 1) {
                append("a")
            }
        }

        viewModel.insertShoppingItem(invalidName, "25", "24.50")

        val value = viewModel.getShoppingItemStatus().getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)

    }

    @Test
    fun `insert shopping item with too long price, returns error`() {

        val invalidPrice = buildString {
            val maxLength = Constants.MAX_PRICE_LENGTH
            for (i in 1..maxLength + 1) {
                append("1")
            }
        }

        viewModel.insertShoppingItem("name", "25", invalidPrice)

        val value = viewModel.getShoppingItemStatus().getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)

    }

    @Test
    fun `insert shopping item with too high amount, returns error`() {

        val invalidAmount = "999999999999999999999"

        viewModel.insertShoppingItem("name", invalidAmount, "2.5")

        val value = viewModel.getShoppingItemStatus().getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)

    }

    @Test
    fun `insert shopping item with valid inputs, returns success`() {

        viewModel.insertShoppingItem("name", "24", "2.5")

        val value = viewModel.getShoppingItemStatus().getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)

    }

}