package com.andef.shoppinglist.domain.usecases

import android.content.Context
import com.andef.shoppinglist.data.repository.ShopItemsRepositoryImpl
import com.andef.shoppinglist.domain.entities.ShopItem
import io.reactivex.Completable

class ChangeShopItemUseCase(context: Context) {
    private val repository = ShopItemsRepositoryImpl.getInstance(context)

    fun execute(
        shopItem: ShopItem,
        newName: String,
        newCount: Int,
        newIsActive: Boolean
    ): Completable {
        return repository.changeShopItem(shopItem, newName, newCount, newIsActive)
    }
}