package com.andef.shoppinglist.data.datasource

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ShopItem")
class ShopItemDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val count: Int,
    var isActive: Boolean
)