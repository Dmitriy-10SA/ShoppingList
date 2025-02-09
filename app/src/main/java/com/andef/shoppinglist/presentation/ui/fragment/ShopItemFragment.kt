package com.andef.shoppinglist.presentation.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.andef.shoppinglist.R
import com.andef.shoppinglist.presentation.ui.viewmodel.ShopItemViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ShopItemFragment : Fragment() {
    private lateinit var textInputLayoutName: TextInputLayout
    private lateinit var textInputLayoutCount: TextInputLayout

    private lateinit var textInputEditTextName: TextInputEditText
    private lateinit var textInputEditTextCount: TextInputEditText

    private lateinit var buttonSave: Button

    private lateinit var viewModel: ShopItemViewModel

    private lateinit var onSaveFragmentChange: OnSaveFragmentChange

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnSaveFragmentChange) {
            onSaveFragmentChange = object : OnSaveFragmentChange {
                override fun onSave() {
                    context.onSave()
                }
            }
        } else {
            throw RuntimeException("Not OnSaveFragmentChange interface in $this")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]

        if (arguments?.getString(SCREEN_MODE) == ADD_SCREEN_MODE) {
            initForAdd()
        } else if (arguments?.getString(SCREEN_MODE) == CHANGE_SCREEN_MODE) {
            initForChange()
        } else {
            showErrorToast()
            onSaveFragmentChange.onSave()
        }
    }

    private fun showErrorToast() {
        Toast.makeText(
            context,
            R.string.error_replay,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }

    private fun initForAdd() {
        initForIntersect()

        viewModel.isSuccessAdd.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                onSaveFragmentChange.onSave()
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

        viewModel.isSuccessChange.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                textInputLayoutName.isErrorEnabled = false
                textInputLayoutCount.isErrorEnabled = false
                onSaveFragmentChange.onSave()
            } else {
                showErrorToast()
            }
        }

        buttonSave.setOnClickListener {
            val id = arguments?.getInt(SHOP_ITEM_ID)!!
            val name = textInputEditTextName.text.toString().trim()
            val count = textInputEditTextCount.text.toString().trim()
            val isActive = arguments?.getBoolean(SHOP_ITEM_IS_ACTIVE)!!
            viewModel.changeShopItem(id, name, count, isActive)
        }
    }

    private fun setTextFields() {
        val name = arguments?.getString(SHOP_ITEM_NAME)
        val count = arguments?.getInt(SHOP_ITEM_COUNT).toString()
        textInputEditTextName.setText(name)
        textInputEditTextCount.setText(count)
    }

    private fun initForIntersect() {
        textInputLayoutName = requireView().findViewById(R.id.textInputLayoutName)
        textInputLayoutCount = requireView().findViewById(R.id.textInputLayoutCount)

        textInputEditTextName = requireView().findViewById(R.id.textInputEditTextName)
        textInputEditTextCount = requireView().findViewById(R.id.textInputEditTextCount)

        setTextListeners()

        buttonSave = requireView().findViewById(R.id.buttonSave)

        initViewModelForIntersect()
    }

    private fun initViewModelForIntersect() {
        viewModel.isNotRightName.observe(viewLifecycleOwner) {
            textInputLayoutName.error = getString(R.string.input_name_error)
            textInputLayoutName.isErrorEnabled = true
        }
        viewModel.isNotRightCount.observe(viewLifecycleOwner) {
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

    companion object {
        private const val SCREEN_MODE = "screenMode"
        private const val ADD_SCREEN_MODE = "addMode"
        private const val CHANGE_SCREEN_MODE = "changeMode"
        private const val SHOP_ITEM_ID = "shopItemId"
        private const val SHOP_ITEM_NAME = "shopItemName"
        private const val SHOP_ITEM_COUNT = "shopItemCount"
        private const val SHOP_ITEM_IS_ACTIVE = "shopItemIsActive"

        fun newInstanceAddShopItem(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, ADD_SCREEN_MODE)
                }
            }
        }

        fun newInstanceChangeShopItem(
            id: Int,
            name: String,
            count: Int,
            isActive: Boolean
        ): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, CHANGE_SCREEN_MODE)
                    putInt(SHOP_ITEM_ID, id)
                    putString(SHOP_ITEM_NAME, name)
                    putInt(SHOP_ITEM_COUNT, count)
                    putBoolean(SHOP_ITEM_IS_ACTIVE, isActive)
                }
            }
        }
    }
}