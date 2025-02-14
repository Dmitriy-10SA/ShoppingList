package com.andef.shoppinglist.domain.repository

import androidx.lifecycle.LiveData
import com.andef.shoppinglist.domain.entities.ShopItem

interface ShopItemsRepository {
    suspend fun addShopItem(shopItem: ShopItem)
    suspend fun changeShopItem(
        shopItem: ShopItem,
        newName: String,
        newCount: Int,
        newIsActive: Boolean
    )

    suspend fun removeShopItem(shopItem: ShopItem)
    fun getAllShopItems(): LiveData<List<ShopItem>>
}