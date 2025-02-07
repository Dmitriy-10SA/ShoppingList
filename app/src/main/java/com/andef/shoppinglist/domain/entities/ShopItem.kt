package com.andef.shoppinglist.domain.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "ShopItem")
data class ShopItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val count: Int,
    var isActive: Boolean
) {
    @Ignore
    constructor(name: String, count: Int) : this(0, name, count, true)
}
