package com.andef.shoppinglist.data.mappers

import com.andef.shoppinglist.data.datasource.ShopItemDbModel
import com.andef.shoppinglist.domain.entities.ShopItem
import javax.inject.Inject

class ShopItemMapper @Inject constructor() {
    fun mapShopItemToDbModel(shopItem: ShopItem): ShopItemDbModel {
        return ShopItemDbModel(shopItem.id, shopItem.name, shopItem.count, shopItem.isActive)
    }

    private fun mapDbToShopItem(db: ShopItemDbModel): ShopItem {
        return ShopItem(db.id, db.name, db.count, db.isActive)
    }

    fun mapListDbToListShopItem(shopItemsDb: List<ShopItemDbModel>): List<ShopItem> {
        return shopItemsDb.map { mapDbToShopItem(it) }
    }
}