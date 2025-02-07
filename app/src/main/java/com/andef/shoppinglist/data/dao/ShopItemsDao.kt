package com.andef.shoppinglist.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.andef.shoppinglist.domain.entities.ShopItem
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ShopItemsDao {
    @Insert
    fun addShopItem(shopItem: ShopItem): Completable

    @Query("UPDATE shopitem SET name = :newName, count = :newCount, isActive = :newIsActive WHERE id = :id")
    fun changeShopItem(id: Int, newName: String, newCount: Int, newIsActive: Boolean): Completable

    @Query("DELETE FROM shopitem WHERE id = :id")
    fun removeShopItem(id: Int): Completable

    @Query("SELECT * FROM shopitem ORDER BY id ASC")
    fun getAllShopItems(): LiveData<List<ShopItem>>
}