package com.andef.shoppinglist.presentation.app

import android.app.Application
import com.andef.shoppinglist.di.DaggerShoppingListComponent

class ShoppingListApplication : Application() {
    val component by lazy {
        DaggerShoppingListComponent.factory().create(this)
    }
}