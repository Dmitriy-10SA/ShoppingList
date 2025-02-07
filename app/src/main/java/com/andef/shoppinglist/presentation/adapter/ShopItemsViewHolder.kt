package com.andef.shoppinglist.presentation.adapter

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.andef.shoppinglist.R

class ShopItemsViewHolder(itemView: View) : ViewHolder(itemView) {
    val cardViewShopItem = itemView.findViewById<CardView>(R.id.cardViewShopItem)
    val textViewName = itemView.findViewById<TextView>(R.id.textViewName)
    val textViewCount = itemView.findViewById<TextView>(R.id.textViewCount)
}