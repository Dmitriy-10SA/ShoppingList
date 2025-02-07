package com.andef.shoppinglist.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.andef.shoppinglist.R
import com.andef.shoppinglist.domain.entities.ShopItem
import com.andef.shoppinglist.presentation.adapterListeners.OnShopItemClickListener
import com.andef.shoppinglist.presentation.adapterListeners.OnShopItemLongClickListener

class ShopItemsAdapter : ListAdapter<ShopItem, ShopItemsViewHolder>(ShopItemsCallback()) {
    private var onShopItemClickListener: OnShopItemClickListener? = null
    private var onShopItemLongClickListener: OnShopItemLongClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemsViewHolder {
        val resId = if (viewType == VIEW_ACTIVE) {
            R.layout.shope_item_active
        } else {
            R.layout.shope_item_not_active
        }
        val view = LayoutInflater.from(parent.context).inflate(resId, parent, false)
        return ShopItemsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopItemsViewHolder, position: Int) {
        val shopItem = getItem(position)
        holder.textViewCount.text = "${shopItem.count}"
        holder.textViewName.text = shopItem.name
        holder.cardViewShopItem.setOnClickListener {
            onShopItemClickListener?.onClick(shopItem)
        }
        holder.cardViewShopItem.setOnLongClickListener {
            onShopItemLongClickListener?.onClick(shopItem)
            true
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).isActive) {
            VIEW_ACTIVE
        } else {
            VIEW_NOT_ACTIVE
        }
    }

    fun setOnShopItemClickListener(callback: (ShopItem) -> Unit) {
        onShopItemClickListener = OnShopItemClickListener { shopItem ->
            callback(shopItem)
        }
    }

    fun setOnShopItemLongClickListener(callback: (ShopItem) -> Unit) {
        onShopItemLongClickListener = OnShopItemLongClickListener { shopItem ->
            callback(shopItem)
        }
    }

    companion object {
        private const val VIEW_ACTIVE = 1
        private const val VIEW_NOT_ACTIVE = 2
    }
}