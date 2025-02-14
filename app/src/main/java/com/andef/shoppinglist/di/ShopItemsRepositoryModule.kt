package com.andef.shoppinglist.di

import android.app.Application
import com.andef.shoppinglist.data.datasource.ShopItemsDataBase
import com.andef.shoppinglist.data.mappers.ShopItemMapper
import com.andef.shoppinglist.data.repository.ShopItemsRepositoryImpl
import com.andef.shoppinglist.domain.repository.ShopItemsRepository
import dagger.Module
import dagger.Provides

@Module
class ShopItemsRepositoryModule {
    @Provides
    fun provideShopItemsDataBase(application: Application): ShopItemsDataBase {
        return ShopItemsDataBase.getInstance(application)
    }

    @Provides
    fun provideShopItemsRepository(
        dataBase: ShopItemsDataBase,
        shopItemMapper: ShopItemMapper
    ): ShopItemsRepository {
        return ShopItemsRepositoryImpl.getInstance(dataBase, shopItemMapper)
    }
}