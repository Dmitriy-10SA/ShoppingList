package com.andef.shoppinglist.presentation.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.andef.shoppinglist.R
import com.andef.shoppinglist.databinding.ActivityMainBinding
import com.andef.shoppinglist.presentation.adapter.ShopItemsAdapter
import com.andef.shoppinglist.presentation.app.ShoppingListApplication
import com.andef.shoppinglist.presentation.factory.ViewModelFactory
import com.andef.shoppinglist.presentation.ui.fragment.OnSaveFragmentChange
import com.andef.shoppinglist.presentation.ui.fragment.ShopItemFragment
import com.andef.shoppinglist.presentation.ui.viewmodel.MainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import javax.inject.Inject

class MainActivity : AppCompatActivity(), OnSaveFragmentChange {
    private val component by lazy {
        (application as ShoppingListApplication).component
    }
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var shopItemsAdapter: ShopItemsAdapter

    private lateinit var itemTouchHelper: ItemTouchHelper

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initAll()
    }

    override fun onSave() {
        supportFragmentManager.popBackStack()
    }

    private fun initAll() {
        initViews()
        initViewModelObserves()
        initItemTouchHelper()
    }

    private fun initItemTouchHelper() {
        itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val shopItem = shopItemsAdapter.currentList[viewHolder.adapterPosition]
                viewModel.removeShopItem(shopItem)
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewAllShopItems)
    }

    private fun initViewModelObserves() {
        viewModel.error.observe(this) {
            showErrorToast()
        }
        viewModel.getAllShopItems.observe(this) { shopItems ->
            shopItemsAdapter.submitList(shopItems)
        }
    }

    private fun showErrorToast() {
        Toast.makeText(
            this,
            R.string.error_replay,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun initViews() {
        initRecyclerViewAndAdapter()
        initFloatingActionButton()
    }

    private fun initRecyclerViewAndAdapter() {
        shopItemsAdapter = ShopItemsAdapter()
        binding.recyclerViewAllShopItems.adapter = shopItemsAdapter

        initShopItemListeners()
    }

    private fun initShopItemListeners() {
        shopItemsAdapter.setOnShopItemClickListener { shopItem ->
            if (isFragmentMode()) {
                val fragment = ShopItemFragment.newInstanceChangeShopItem(
                    shopItem.id,
                    shopItem.name,
                    shopItem.count,
                    shopItem.isActive
                )
                supportFragmentManager.popBackStack()
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainerViewMain, fragment)
                    .addToBackStack(null)
                    .commit()
            } else {
                val intent = ShopItemActivity.newIntentChangeShopItem(this@MainActivity, shopItem)
                startActivity(intent)
            }
        }
        shopItemsAdapter.setOnShopItemLongClickListener { shopItem ->
            viewModel.changeShopItemActiveState(shopItem, !shopItem.isActive)
        }
    }

    private fun initFloatingActionButton() {
        binding.floatingActionButtonAddShopItem.setOnClickListener {
            if (isFragmentMode()) {
                val fragment = ShopItemFragment.newInstanceAddShopItem(isFragmentMode())
                supportFragmentManager.popBackStack()
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainerViewMain, fragment)
                    .addToBackStack(null)
                    .commit()
            } else {
                val intent = ShopItemActivity.newIntentAddShopItem(this)
                startActivity(intent)
            }
        }
    }

    private fun isFragmentMode(): Boolean = binding.fragmentContainerViewMain != null
}