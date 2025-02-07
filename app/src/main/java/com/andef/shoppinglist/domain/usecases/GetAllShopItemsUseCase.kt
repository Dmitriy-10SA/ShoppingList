package com.andef.shoppinglist.domain.usecases

import android.content.Context
import androidx.lifecycle.LiveData
import com.andef.shoppinglist.data.repository.ShopItemsRepositoryImpl
import com.andef.shoppinglist.domain.entities.ShopItem

class GetAllShopItemsUseCase(context: Context) {
    private val repository = ShopItemsRepositoryImpl.getInstance(context)

    fun execute(): LiveData<List<ShopItem>> {
        return repository.getAllShopItems()
    }
}