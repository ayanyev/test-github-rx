package com.ezzyapps.test.repositories.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.annotation.IdRes
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.ezzyapps.test.repositories.BR

@BindingAdapter("visible")
fun View.setVisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("items", "layout")
fun <T> ListView.createList(
    entries: List<T>?,
    @IdRes layoutId: Int
) {
    entries?.let {
        adapter = object : ArrayAdapter<T>(context, layoutId, entries) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                return (convertView?.tag as? ViewDataBinding
                    ?: DataBindingUtil.inflate(
                        LayoutInflater.from(context), layoutId, null, true
                    )
                        ).apply {
                        setVariable(BR.viewModel, entries[position])
                        root.tag = this
                    }.root
            }
        }
    }
}