package com.andef.shoppinglist.presentation.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.andef.shoppinglist.R
import com.andef.shoppinglist.domain.entities.ShopItem
import com.andef.shoppinglist.presentation.ui.viewmodel.ShopItemViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ShopItemActivity : AppCompatActivity() {
    private lateinit var textInputLayoutName: TextInputLayout
    private lateinit var textInputLayoutCount: TextInputLayout

    private lateinit var textInputEditTextName: TextInputEditText
    private lateinit var textInputEditTextCount: TextInputEditText

    private lateinit var buttonSave: Button

    private lateinit var viewModel: ShopItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]

        if (intent.extras?.getString(EXTRA_SCREEN_MODE) == ADD_SCREEN_MODE) {
            initForAdd()
        } else if (intent.extras?.getString(EXTRA_SCREEN_MODE) == CHANGE_SCREEN_MODE) {
            initForChange()
        } else {
            showErrorToast()
            finish()
        }
    }

    private fun initForAdd() {
        initForIntersect()

        viewModel.isSuccessAdd.observe(this) { isSuccess ->
            if (isSuccess) {
                finish()
            } else {
                showErrorToast()
            }
        }

        buttonSave.setOnClickListener {
            val name = textInputEditTextName.text.toString().trim()
            val count = textInputEditTextCount.text.toString().trim()
            viewModel.addShopItem(name, count)
        }
    }

    private fun initForChange() {
        initForIntersect()

        setTextFields()

        viewModel.isSuccessChange.observe(this) { isSuccess ->
            if (isSuccess) {
                textInputLayoutName.isErrorEnabled = false
                textInputLayoutCount.isErrorEnabled = false
                finish()
            } else {
                showErrorToast()
            }
        }

        buttonSave.setOnClickListener {
            val id = intent.extras?.getInt(EXTRA_SHOP_ITEM_ID)!!
            val name = textInputEditTextName.text.toString().trim()
            val count = textInputEditTextCount.text.toString().trim()
            val isActive = intent.extras?.getBoolean(EXTRA_SHOP_ITEM_IS_ACTIVE)!!
            viewModel.changeShopItem(id, name, count, isActive)
        }
    }

    private fun setTextFields() {
        val name = intent.extras?.getString(EXTRA_SHOP_ITEM_NAME)
        val count = intent.extras?.getInt(EXTRA_SHOP_ITEM_COUNT).toString()
        textInputEditTextName.setText(name)
        textInputEditTextCount.setText(count)
    }

    private fun initForIntersect() {
        textInputLayoutName = findViewById(R.id.textInputLayoutName)
        textInputLayoutCount = findViewById(R.id.textInputLayoutCount)

        textInputEditTextName = findViewById(R.id.textInputEditTextName)
        textInputEditTextCount = findViewById(R.id.textInputEditTextCount)

        setTextListeners()

        buttonSave = findViewById(R.id.buttonSave)

        initViewModelForIntersect()
    }

    private fun initViewModelForIntersect() {
        viewModel.isNotRightName.observe(this) {
            textInputLayoutName.error = getString(R.string.input_name_error)
            textInputLayoutName.isErrorEnabled = true
        }
        viewModel.isNotRightCount.observe(this) {
            textInputLayoutCount.error = getString(R.string.input_count_error)
            textInputLayoutCount.isErrorEnabled = true
        }
    }

    private fun setTextListeners() {
        textInputEditTextName.addTextChangedListener {
            if (it.toString().isNotEmpty()) {
                textInputLayoutName.isErrorEnabled = false
            }
        }

        textInputEditTextCount.addTextChangedListener {
            if (it.toString().isNotEmpty()) {
                textInputLayoutCount.isErrorEnabled = false
            }
        }
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