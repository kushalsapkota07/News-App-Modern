package com.example.newsappmodern.adapters

import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

import com.example.newsappmodern.databinding.DropdownItemCategoryBinding
import com.example.newsappmodern.util.Category

class NewsCategoryDropdownMenuAdapter(
    context: Context
) : ArrayAdapter<Category>(context, 0, Category.entries.toTypedArray()) {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: DropdownItemCategoryBinding
        if (convertView == null) {
            binding = DropdownItemCategoryBinding.inflate(layoutInflater, parent, false)
            binding.root.tag = binding
        } else {
            binding = convertView.tag as DropdownItemCategoryBinding
        }
        getItem(position)?.let { category ->
            setItemForCategory(binding, category)
        }
        return binding.root
    }
        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            val binding:DropdownItemCategoryBinding
            if (position == 0) {
                binding = DropdownItemCategoryBinding.inflate(layoutInflater, parent, false)
                binding.root.setOnClickListener {
                    val root = parent.rootView
                    root.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK))
                    root.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK))
                }
            } else {
                binding = DropdownItemCategoryBinding.inflate(layoutInflater, parent, false)
                getItem(position)?.let { category ->
                    setItemForCategory(binding, category)
                }
            }
            return binding.root
        }

        override fun getItem(position: Int): Category? {
            if (position == 0) {
                return null
            }
            return super.getItem(position - 1)
        }

        override fun getCount() = super.getCount() + 1

        override fun isEnabled(position: Int) = position != 0

        private fun setItemForCategory(binding: DropdownItemCategoryBinding, category:Category) {
            binding.tvCategory.text = category.cat.replaceFirstChar { it.uppercaseChar() }
        }
    }

