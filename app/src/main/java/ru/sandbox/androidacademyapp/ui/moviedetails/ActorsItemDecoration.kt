package ru.sandbox.androidacademyapp.ui.moviedetails

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ActorsItemDecoration(val space: Int): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
        if (itemPosition > 0) outRect.left =  space
        outRect.right = space
    }

}