package ru.sandbox.androidacademyapp.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MoviesItemDecoration(val space: Int, val columnNumber: Int ): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
            val column = itemPosition % columnNumber
            outRect.left = column * space / columnNumber
            outRect.right = space - (column + 1) * space / columnNumber
            if (itemPosition < columnNumber) { outRect.top = space }
            outRect.bottom = space
    }

}