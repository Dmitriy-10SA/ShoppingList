package com.andef.shoppinglist.di

import androidx.lifecycle.ViewModel
import com.andef.shoppinglist.presentation.ui.viewmodel.MainViewModel
import com.andef.shoppinglist.presentation.ui.viewmodel.ShopItemViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    @Binds
    fun bindMainViewModel(impl: MainViewModel): ViewModel

    @IntoMap
    @ViewModelKey(ShopItemViewModel::class)
    @Binds
    fun bindShopItemViewModel(impl: ShopItemViewModel): ViewModel
}