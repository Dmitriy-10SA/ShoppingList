package com.andef.shoppinglist.presentation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andef.shoppinglist.domain.entities.ShopItem
import com.andef.shoppinglist.domain.usecases.AddShopItemUseCase
import com.andef.shoppinglist.domain.usecases.ChangeShopItemUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShopItemViewModel @Inject constructor(
    private val changeShopItemUseCase: ChangeShopItemUseCase,
    private val addShopItemUseCase: AddShopItemUseCase
) : ViewModel() {
    private val _isSuccessAdd = MutableLiveData<Boolean>()
    val isSuccessAdd: LiveData<Boolean> = _isSuccessAdd

    private val _isSuccessChange = MutableLiveData<Boolean>()
    val isSuccessChange: LiveData<Boolean> = _isSuccessChange

    private val _isNotRightName = MutableLiveData<Unit>()
    val isNotRightName: LiveData<Unit> = _isNotRightName

    private val _isNotRightCount = MutableLiveData<Unit>()
    val isNotRightCount: LiveData<Unit> = _isNotRightCount

    private val addExceptionHandler = CoroutineExceptionHandler { _, _ ->
        _isSuccessAdd.value = false
    }

    private val changeExceptionHandler = CoroutineExceptionHandler { _, _ ->
        _isSuccessChange.value = false
    }

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
        viewModelScope.launch(addExceptionHandler) {
            if (!isActive) throw CancellationException()
            addShopItemUseCase.execute(shopItem)
            _isSuccessAdd.value = true
        }
    }

    fun changeShopItem(id: Int, newName: String, newCount: String, newIsActive: Boolean) {
        if (!isRightNameAndCount(newName, newCount)) {
            return
        }
        val shopItem = ShopItem(id, newName, newCount.toInt(), newIsActive)
        viewModelScope.launch(changeExceptionHandler) {
            if (!isActive) throw CancellationException()
            changeShopItemUseCase.execute(shopItem, newName, newCount.toInt(), newIsActive)
            _isSuccessChange.value = true
        }
    }
}