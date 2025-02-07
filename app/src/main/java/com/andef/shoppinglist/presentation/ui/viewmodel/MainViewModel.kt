package com.andef.shoppinglist.presentation.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andef.shoppinglist.domain.entities.ShopItem
import com.andef.shoppinglist.domain.usecases.ChangeShopItemUseCase
import com.andef.shoppinglist.domain.usecases.GetAllShopItemsUseCase
import com.andef.shoppinglist.domain.usecases.RemoveShopItemUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val compositeDisposable = CompositeDisposable()

    private val getAllShopItemsUseCase = GetAllShopItemsUseCase(application)
    private val removeShopItemUseCase = RemoveShopItemUseCase(application)
    private val changeShopItemUseCase = ChangeShopItemUseCase(application)

    val getAllShopItems = getAllShopItemsUseCase.execute()

    private val _error = MutableLiveData<Unit>()
    val error: LiveData<Unit> = _error

    fun changeShopItemActiveState(
        shopItem: ShopItem,
        newIsActive: Boolean
    ) {
        val disposable = changeShopItemUseCase.execute(
            shopItem,
            shopItem.name,
            shopItem.count,
            newIsActive
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {},
                { _error.value = Unit }
            )
        compositeDisposable.add(disposable)
    }

    fun removeShopItem(shopItem: ShopItem) {
        val disposable = removeShopItemUseCase.execute(shopItem)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {},
                { _error.value = Unit }
            )
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}