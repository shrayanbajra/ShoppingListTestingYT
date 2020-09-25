package com.androiddevs.shoppinglisttestingyt.di

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItemDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Named

@Module
@InstallIn(ApplicationComponent::class)
object TestAppModule {

    @Provides
    @Named("test_db")
    fun provideInMemoryDb(@ApplicationContext context: Context): ShoppingItemDatabase {
        return Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), ShoppingItemDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
    }

}