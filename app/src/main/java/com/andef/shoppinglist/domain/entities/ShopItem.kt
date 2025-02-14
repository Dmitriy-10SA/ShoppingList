package com.andef.shoppinglist.domain.entities

data class ShopItem(
    val id: Int,
    val name: String,
    val count: Int,
    var isActive: Boolean
) {
    constructor(name: String, count: Int) : this(0, name, count, true)
}
