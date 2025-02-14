package com.andef.shoppinglist.presentation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andef.shoppinglist.domain.entities.ShopItem
import com.andef.shoppinglist.domain.usecases.ChangeShopItemUseCase
import com.andef.shoppinglist.domain.usecases.GetAllShopItemsUseCase
import com.andef.shoppinglist.domain.usecases.RemoveShopItemUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getAllShopItemsUseCase: GetAllShopItemsUseCase,
    private val removeShopItemUseCase: RemoveShopItemUseCase,
    private val changeShopItemUseCase: ChangeShopItemUseCase
) : ViewModel() {
    val getAllShopItems = getAllShopItemsUseCase.execute()

    private val _error = MutableLiveData<Unit>()
    val error: LiveData<Unit> = _error

    private val errorExceptionHandler = CoroutineExceptionHandler { _, _ ->
        _error.value = Unit
    }

    fun changeShopItemActiveState(
        shopItem: ShopItem,
        newIsActive: Boolean
    ) {
        viewModelScope.launch(errorExceptionHandler) {
            if (!isActive) throw CancellationException()
            changeShopItemUseCase.execute(
                shopItem,
                shopItem.name,
                shopItem.count,
                newIsActive
            )
        }
    }

    fun removeShopItem(shopItem: ShopItem) {
        viewModelScope.launch(errorExceptionHandler) {
            if (!isActive) throw CancellationException()
            removeShopItemUseCase.execute(shopItem)
        }
    }
}