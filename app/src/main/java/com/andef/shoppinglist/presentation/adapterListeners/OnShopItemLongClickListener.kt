package com.andef.shoppinglist.presentation.adapterListeners

import com.andef.shoppinglist.domain.entities.ShopItem

fun interface OnShopItemLongClickListener {
    fun onClick(shopItem: ShopItem)
}