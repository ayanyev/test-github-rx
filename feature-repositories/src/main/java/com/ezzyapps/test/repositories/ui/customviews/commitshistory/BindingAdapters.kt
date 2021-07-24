package com.ezzyapps.test.repositories.ui.customviews.commitshistory

import android.animation.ValueAnimator
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.children
import androidx.databinding.BindingAdapter

@BindingAdapter("count", "maxCount")
fun TextView.animateHeight(count: Int, maxCount: Int) {

    if (count == 0 || maxCount == 0) return

    val availableHeight: Float? = (tag as? Int)?.toFloat()

    if (availableHeight == null) {
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                tag = (parent as LinearLayoutCompat).run {
                    val c = children.toList()
                    // available height - parent height minus heights of other sibling views
                    height - c[0].height - c[2].height
                }
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                animateHeight(count, maxCount)
            }
        })
        return
    }

    val from = height
    val to = (availableHeight * (count.toFloat() / maxCount.toFloat())).toInt()

    ValueAnimator.ofInt(from, to).apply {
        addUpdateListener { updatedAnimation ->
            height = updatedAnimation.animatedValue as Int
        }
        duration = 1000
        start()
    }

}