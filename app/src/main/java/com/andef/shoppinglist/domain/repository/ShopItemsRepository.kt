package com.andef.shoppinglist.domain.repository

import androidx.lifecycle.LiveData
import com.andef.shoppinglist.domain.entities.ShopItem
import io.reactivex.Completable
import io.reactivex.Single

interface ShopItemsRepository {
    fun addShopItem(shopItem: ShopItem): Completable
    fun changeShopItem(
        shopItem: ShopItem,
        newName: String,
        newCount: Int,
        newIsActive: Boolean
    ): Completable
    fun removeShopItem(shopItem: ShopItem): Completable
    fun getAllShopItems(): LiveData<List<ShopItem>>
}