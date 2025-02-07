package com.andef.shoppinglist.presentation.adapterListeners

import com.andef.shoppinglist.domain.entities.ShopItem

fun interface OnShopItemClickListener {
    fun onClick(shopItem: ShopItem)
}