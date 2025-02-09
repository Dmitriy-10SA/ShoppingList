package com.andef.shoppinglist.presentation.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.andef.shoppinglist.R
import com.andef.shoppinglist.domain.entities.ShopItem
import com.andef.shoppinglist.presentation.ui.fragment.OnSaveFragmentChange
import com.andef.shoppinglist.presentation.ui.fragment.ShopItemFragment

class ShopItemActivity : AppCompatActivity(), OnSaveFragmentChange {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)

        beginFragmentForType()
    }

    override fun onSave() {
        finish()
    }

    private fun beginFragmentForType() {
        if (intent.extras?.getString(EXTRA_SCREEN_MODE) == ADD_SCREEN_MODE) {
            startFragment(ADD_SCREEN_MODE)
        } else if (intent.extras?.getString(EXTRA_SCREEN_MODE) == CHANGE_SCREEN_MODE) {
            val id = intent.extras?.getInt(EXTRA_SHOP_ITEM_ID)
            val name = intent.extras?.getString(EXTRA_SHOP_ITEM_NAME)
            val count = intent.extras?.getInt(EXTRA_SHOP_ITEM_COUNT)
            val isActive = intent.extras?.getBoolean(EXTRA_SHOP_ITEM_IS_ACTIVE)
            if (id == null || name == null || count == null || isActive == null) {
                showErrorToast()
                finish()
            } else {
                startFragment(CHANGE_SCREEN_MODE, id, name, count, isActive)
            }
        } else {
            showErrorToast()
            finish()
        }
    }

    private fun startFragment(
        screenMode: String,
        id: Int = 0,
        name: String = "",
        count: Int = 0,
        isActive: Boolean = false
    ) {
        val fragment = if (screenMode == ADD_SCREEN_MODE) {
            ShopItemFragment.newInstanceAddShopItem()
        } else {
            ShopItemFragment.newInstanceChangeShopItem(id, name, count, isActive)
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fcv_shop_item, fragment)
            .commit()
    }

    private fun showErrorToast() {
        Toast.makeText(
            this,
            R.string.error_replay,
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        private const val EXTRA_SCREEN_MODE = "screenMode"
        private const val ADD_SCREEN_MODE = "addMode"
        private const val CHANGE_SCREEN_MODE = "changeMode"
        private const val EXTRA_SHOP_ITEM_ID = "shopItemId"
        private const val EXTRA_SHOP_ITEM_NAME = "shopItemName"
        private const val EXTRA_SHOP_ITEM_COUNT = "shopItemCount"
        private const val EXTRA_SHOP_ITEM_IS_ACTIVE = "shopItemIsActive"

        fun newIntentAddShopItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, ADD_SCREEN_MODE)
            return intent
        }

        fun newIntentChangeShopItem(context: Context, shopItem: ShopItem): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, CHANGE_SCREEN_MODE)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItem.id)
            intent.putExtra(EXTRA_SHOP_ITEM_NAME, shopItem.name)
            intent.putExtra(EXTRA_SHOP_ITEM_COUNT, shopItem.count)
            intent.putExtra(EXTRA_SHOP_ITEM_IS_ACTIVE, shopItem.isActive)
            return intent
        }
    }
}