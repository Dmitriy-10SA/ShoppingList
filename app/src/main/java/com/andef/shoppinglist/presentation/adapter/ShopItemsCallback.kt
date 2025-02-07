package com.andef.shoppinglist.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.andef.shoppinglist.domain.entities.ShopItem

class ShopItemsCallback : DiffUtil.ItemCallback<ShopItem>() {
    override fun areItemsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
        return oldItem == newItem
    }
}