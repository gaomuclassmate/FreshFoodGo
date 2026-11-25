package com.quick.freshfoodgo.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * 水平间距
 */
class HorizontalSpaceDecoration(private val spaceDp: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val density = parent.resources.displayMetrics.density
        val spacePx = (spaceDp * density).toInt()
        val pos = parent.getChildAdapterPosition(view)
        val total = parent.adapter?.itemCount ?: 0

        // 只有右边加间距，第一个item左边不留空隙
        outRect.right = spacePx
        outRect.left = 0
        outRect.top = 0
        outRect.bottom = 0
    }
}