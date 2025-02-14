package com.andef.shoppinglist.di

import com.andef.shoppinglist.data.dao.ShopItemsDao
import com.andef.shoppinglist.data.datasource.ShopItemsDataBase
import dagger.Module
import dagger.Provides

@Module
class ShopItemsDaoModule {
    @Provides
    fun provideShopItemsDao(dataBase: ShopItemsDataBase): ShopItemsDao {
        return dataBase.dao
    }
}