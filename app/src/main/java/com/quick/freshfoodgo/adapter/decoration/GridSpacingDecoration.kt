package com.quick.freshfoodgo.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacingDecoration(
    private val spanCount: Int,
    private val spacing: Int, // dp
    private val includeEdge: Boolean
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) return
        val column = position % spanCount

        // dp转px
        val density = view.resources.displayMetrics.density
        val spacePx = (spacing * density + 0.5f).toInt()

        if (includeEdge) {
            outRect.left = spacePx - column * spacePx / spanCount
            outRect.right = (column + 1) * spacePx / spanCount
            if (position < spanCount) {
                outRect.top = spacePx
            }
            outRect.bottom = spacePx
        } else {
            outRect.left = column * spacePx / spanCount
            outRect.right = spacePx - (column + 1) * spacePx / spanCount
            if (position >= spanCount) {
                outRect.top = spacePx
            }
        }
    }
}
