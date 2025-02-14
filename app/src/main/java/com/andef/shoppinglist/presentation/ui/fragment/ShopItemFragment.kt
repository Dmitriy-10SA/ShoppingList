package com.andef.shoppinglist.presentation.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.andef.shoppinglist.R
import com.andef.shoppinglist.databinding.FragmentShopItemBinding
import com.andef.shoppinglist.presentation.app.ShoppingListApplication
import com.andef.shoppinglist.presentation.factory.ViewModelFactory
import com.andef.shoppinglist.presentation.ui.viewmodel.ShopItemViewModel
import javax.inject.Inject

class ShopItemFragment : Fragment() {
    private val component by lazy {
        (requireActivity().application as ShoppingListApplication).component
    }
    private var _binding: FragmentShopItemBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ShopItemViewModel::class.java]
    }

    private lateinit var onSaveFragmentChange: OnSaveFragmentChange

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)
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

        if (arguments?.getString(SCREEN_MODE) == ADD_SCREEN_MODE) {
            initForAdd()
        } else if (arguments?.getString(SCREEN_MODE) == CHANGE_SCREEN_MODE) {
            initForChange()
        } else {
            showErrorToast()
            onSaveFragmentChange.onSave()
        }
        if (arguments?.getBoolean(IS_HORIZONTAL_SCREEN) == true) {
            binding.main.background.alpha = 0
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
    ): View {
        _binding = FragmentShopItemBinding.inflate(inflater, container, false)
        return binding.root
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

        binding.buttonSave.setOnClickListener {
            val name = binding.textInputEditTextName.text.toString().trim()
            val count = binding.textInputEditTextCount.text.toString().trim()
            viewModel.addShopItem(name, count)
        }
    }

    private fun initForChange() {
        initForIntersect()

        setTextFields()

        viewModel.isSuccessChange.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                binding.textInputLayoutName.isErrorEnabled = false
                binding.textInputLayoutCount.isErrorEnabled = false
                onSaveFragmentChange.onSave()
            } else {
                showErrorToast()
            }
        }

        binding.buttonSave.setOnClickListener {
            val id = arguments?.getInt(SHOP_ITEM_ID)!!
            val name = binding.textInputEditTextName.text.toString().trim()
            val count = binding.textInputEditTextCount.text.toString().trim()
            val isActive = arguments?.getBoolean(SHOP_ITEM_IS_ACTIVE)!!
            viewModel.changeShopItem(id, name, count, isActive)
        }
    }

    private fun setTextFields() {
        val name = arguments?.getString(SHOP_ITEM_NAME)
        val count = arguments?.getInt(SHOP_ITEM_COUNT).toString()
        binding.textInputEditTextName.setText(name)
        binding.textInputEditTextCount.setText(count)
    }

    private fun initForIntersect() {
        setTextListeners()

        initViewModelForIntersect()
    }

    private fun initViewModelForIntersect() {
        viewModel.isNotRightName.observe(viewLifecycleOwner) {
            binding.textInputLayoutName.error = getString(R.string.input_name_error)
            binding.textInputLayoutName.isErrorEnabled = true
        }
        viewModel.isNotRightCount.observe(viewLifecycleOwner) {
            binding.textInputLayoutCount.error = getString(R.string.input_count_error)
            binding.textInputLayoutCount.isErrorEnabled = true
        }
    }

    private fun setTextListeners() {
        binding.textInputEditTextName.addTextChangedListener {
            if (it.toString().isNotEmpty()) {
                binding.textInputLayoutName.isErrorEnabled = false
            }
        }

        binding.textInputEditTextCount.addTextChangedListener {
            if (it.toString().isNotEmpty()) {
                binding.textInputLayoutCount.isErrorEnabled = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val SCREEN_MODE = "screenMode"
        private const val ADD_SCREEN_MODE = "addMode"
        private const val CHANGE_SCREEN_MODE = "changeMode"
        private const val SHOP_ITEM_ID = "shopItemId"
        private const val SHOP_ITEM_NAME = "shopItemName"
        private const val SHOP_ITEM_COUNT = "shopItemCount"
        private const val SHOP_ITEM_IS_ACTIVE = "shopItemIsActive"
        private const val IS_HORIZONTAL_SCREEN = "isHorizontalScreen"

        fun newInstanceAddShopItem(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, ADD_SCREEN_MODE)
                    putBoolean(IS_HORIZONTAL_SCREEN, false)
                }
            }
        }

        fun newInstanceAddShopItem(isHorizontalScreen: Boolean): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, ADD_SCREEN_MODE)
                    putBoolean(IS_HORIZONTAL_SCREEN, isHorizontalScreen)
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