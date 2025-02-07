package com.andef.shoppinglist.presentation.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andef.shoppinglist.domain.entities.ShopItem
import com.andef.shoppinglist.domain.usecases.AddShopItemUseCase
import com.andef.shoppinglist.domain.usecases.ChangeShopItemUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ShopItemViewModel(application: Application) : AndroidViewModel(application) {
    private val compositeDisposable = CompositeDisposable()

    private val changeShopItemUseCase = ChangeShopItemUseCase(application)
    private val addShopItemUseCase = AddShopItemUseCase(application)

    private val _isSuccessAdd = MutableLiveData<Boolean>()
    val isSuccessAdd: LiveData<Boolean> = _isSuccessAdd

    private val _isSuccessChange = MutableLiveData<Boolean>()
    val isSuccessChange: LiveData<Boolean> = _isSuccessChange

    private val _isNotRightName = MutableLiveData<Unit>()
    val isNotRightName: LiveData<Unit> = _isNotRightName

    private val _isNotRightCount = MutableLiveData<Unit>()
    val isNotRightCount: LiveData<Unit> = _isNotRightCount

    private fun isRightNameAndCount(name: String, count: String): Boolean {
        var flag = true
        if (name.isEmpty()) {
            _isNotRightName.value = Unit
            flag = false
        }
        if (count.isEmpty() || count.contains(".")) {
            _isNotRightCount.value = Unit
            flag = false
        }
        return flag
    }

    fun addShopItem(name: String, count: String) {
        if (!isRightNameAndCount(name, count)) {
            return
        }
        val shopItem = ShopItem(name, count.toInt())
        val disposable = addShopItemUseCase.execute(shopItem)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { _isSuccessAdd.value = true },
                { _isSuccessAdd.value = false }
            )
        compositeDisposable.add(disposable)
    }

    fun changeShopItem(id: Int, newName: String, newCount: String, newIsActive: Boolean) {
        if (!isRightNameAndCount(newName, newCount)) {
            return
        }
        val shopItem = ShopItem(id, newName, newCount.toInt(), newIsActive)
        val disposable = changeShopItemUseCase.execute(shopItem, newName, newCount.toInt(), newIsActive)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { _isSuccessChange.value = true },
                { _isSuccessChange.value = false }
            )
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}