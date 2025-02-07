package com.andef.shoppinglist.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.andef.shoppinglist.data.datasource.ShopItemsDataBase
import com.andef.shoppinglist.domain.entities.ShopItem
import com.andef.shoppinglist.domain.repository.ShopItemsRepository
import io.reactivex.Completable
import io.reactivex.Single

class ShopItemsRepositoryImpl private constructor(context: Context) : ShopItemsRepository {
    private val dataBase = ShopItemsDataBase.getInstance(context)

    override fun addShopItem(shopItem: ShopItem): Completable {
        return dataBase.dao.addShopItem(shopItem)
    }

    override fun changeShopItem(
        shopItem: ShopItem,
        newName: String,
        newCount: Int,
        newIsActive: Boolean
    ): Completable {
        return dataBase.dao.changeShopItem(shopItem.id, newName, newCount, newIsActive)
    }

    override fun removeShopItem(shopItem: ShopItem): Completable {
        return dataBase.dao.removeShopItem(shopItem.id)
    }

    override fun getAllShopItems(): LiveData<List<ShopItem>> {
        return dataBase.dao.getAllShopItems()
    }

    companion object {
        private var instance: ShopItemsRepositoryImpl? = null
        fun getInstance(context: Context): ShopItemsRepositoryImpl {
            if (instance == null) {
                instance = ShopItemsRepositoryImpl(context)
            }
            return instance!!
        }
    }
}