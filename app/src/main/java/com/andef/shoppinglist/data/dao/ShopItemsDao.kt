package com.andef.shoppinglist.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.andef.shoppinglist.data.datasource.ShopItemDbModel

@Dao
interface ShopItemsDao {
    @Insert
    suspend fun addShopItem(shopItem: ShopItemDbModel)

    @Query("UPDATE shopitem SET name = :newName, count = :newCount, isActive = :newIsActive WHERE id = :id")
    suspend fun changeShopItem(id: Int, newName: String, newCount: Int, newIsActive: Boolean)

    @Query("DELETE FROM shopitem WHERE id = :id")
    suspend fun removeShopItem(id: Int)

    @Query("SELECT * FROM shopitem ORDER BY id ASC")
    fun getAllShopItems(): LiveData<List<ShopItemDbModel>>
}