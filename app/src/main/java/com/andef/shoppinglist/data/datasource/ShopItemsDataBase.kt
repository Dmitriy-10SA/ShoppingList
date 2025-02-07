package com.andef.shoppinglist.data.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.andef.shoppinglist.data.dao.ShopItemsDao
import com.andef.shoppinglist.domain.entities.ShopItem

@Database(entities = [ShopItem::class], version = 1, exportSchema = false)
abstract class ShopItemsDataBase: RoomDatabase() {
    abstract val dao: ShopItemsDao

    companion object {
        private var instance: ShopItemsDataBase? = null
        fun getInstance(context: Context): ShopItemsDataBase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context,
                    ShopItemsDataBase::class.java,
                    DB_NAME
                ).build()
            }
            return instance!!
        }

        private const val DB_NAME = "ShopItemsDB"
    }
}