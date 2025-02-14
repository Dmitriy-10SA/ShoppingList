package com.andef.shoppinglist.di

import android.app.Application
import com.andef.shoppinglist.presentation.ui.activity.MainActivity
import com.andef.shoppinglist.presentation.ui.fragment.ShopItemFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [ShopItemsDaoModule::class, ShopItemsRepositoryModule::class, ViewModelModule::class]
)
interface ShoppingListComponent {
    fun inject(activity: MainActivity)
    fun inject(shopItemFragment: ShopItemFragment)

    @Component.Factory
    interface ShoppingListFactory {
        fun create(@BindsInstance application: Application): ShoppingListComponent
    }
}