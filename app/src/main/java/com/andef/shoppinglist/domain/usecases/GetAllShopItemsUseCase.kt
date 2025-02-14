package com.andef.shoppinglist.domain.usecases

import androidx.lifecycle.LiveData
import com.andef.shoppinglist.domain.entities.ShopItem
import com.andef.shoppinglist.domain.repository.ShopItemsRepository
import javax.inject.Inject

class GetAllShopItemsUseCase @Inject constructor(
    private val repository: ShopItemsRepository
) {
    fun execute(): LiveData<List<ShopItem>> {
        return repository.getAllShopItems()
    }
}