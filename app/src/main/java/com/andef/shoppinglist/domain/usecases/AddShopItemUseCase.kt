package com.andef.shoppinglist.domain.usecases

import com.andef.shoppinglist.domain.entities.ShopItem
import com.andef.shoppinglist.domain.repository.ShopItemsRepository
import javax.inject.Inject

class AddShopItemUseCase @Inject constructor(
    private val repository: ShopItemsRepository
) {
    suspend fun execute(shopItem: ShopItem) {
        repository.addShopItem(shopItem)
    }
}