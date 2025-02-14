package com.andef.shoppinglist.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.andef.shoppinglist.data.datasource.ShopItemsDataBase
import com.andef.shoppinglist.data.mappers.ShopItemMapper
import com.andef.shoppinglist.di.ApplicationScope
import com.andef.shoppinglist.domain.entities.ShopItem
import com.andef.shoppinglist.domain.repository.ShopItemsRepository

@ApplicationScope
class ShopItemsRepositoryImpl private constructor(
    private val dataBase: ShopItemsDataBase,
    private val shopItemMapper: ShopItemMapper
) : ShopItemsRepository {

    override suspend fun addShopItem(shopItem: ShopItem) {
        dataBase.dao.addShopItem(shopItemMapper.mapShopItemToDbModel(shopItem))
    }

    override suspend fun changeShopItem(
        shopItem: ShopItem,
        newName: String,
        newCount: Int,
        newIsActive: Boolean
    ) {
        return dataBase.dao.changeShopItem(shopItem.id, newName, newCount, newIsActive)
    }

    override suspend fun removeShopItem(shopItem: ShopItem) {
        dataBase.dao.removeShopItem(shopItem.id)
    }

    override fun getAllShopItems(): LiveData<List<ShopItem>> {
        return MediatorLiveData<List<ShopItem>>().apply {
            addSource(dataBase.dao.getAllShopItems()) {
                value = shopItemMapper.mapListDbToListShopItem(it)
            }
        }
    }

    companion object {
        private var instance: ShopItemsRepositoryImpl? = null
        fun getInstance(
            dataBase: ShopItemsDataBase,
            shopItemMapper: ShopItemMapper
        ): ShopItemsRepositoryImpl {
            if (instance == null) {
                instance = ShopItemsRepositoryImpl(dataBase, shopItemMapper)
            }
            return instance!!
        }
    }
}