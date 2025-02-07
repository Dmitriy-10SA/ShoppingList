package com.andef.shoppinglist.presentation.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.andef.shoppinglist.R
import com.andef.shoppinglist.presentation.adapter.ShopItemsAdapter
import com.andef.shoppinglist.presentation.ui.viewmodel.MainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerViewAllShopItems: RecyclerView
    private lateinit var shopItemsAdapter: ShopItemsAdapter

    private lateinit var floatingActionButtonAddShopItem: FloatingActionButton

    private lateinit var itemTouchHelper: ItemTouchHelper

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initAll()
    }

    private fun initAll() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
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
        itemTouchHelper.attachToRecyclerView(recyclerViewAllShopItems)
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
        recyclerViewAllShopItems = findViewById(R.id.recyclerViewAllShopItems)
        shopItemsAdapter = ShopItemsAdapter()
        recyclerViewAllShopItems.adapter = shopItemsAdapter

        initShopItemListeners()
    }

    private fun initShopItemListeners() {
        shopItemsAdapter.setOnShopItemClickListener { shopItem ->
            val intent = ShopItemActivity.newIntentChangeShopItem(this@MainActivity, shopItem)
            startActivity(intent)
        }
        shopItemsAdapter.setOnShopItemLongClickListener { shopItem ->
            viewModel.changeShopItemActiveState(shopItem, !shopItem.isActive)
        }
    }

    private fun initFloatingActionButton() {
        floatingActionButtonAddShopItem = findViewById(R.id.floatingActionButtonAddShopItem)
        floatingActionButtonAddShopItem.setOnClickListener {
            val intent = ShopItemActivity.newIntentAddShopItem(this)
            startActivity(intent)
        }
    }
}