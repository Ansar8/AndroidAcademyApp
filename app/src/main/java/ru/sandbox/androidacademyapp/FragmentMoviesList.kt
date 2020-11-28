package ru.sandbox.androidacademyapp

import android.content.Context
import android.graphics.Outline
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout

class FragmentMoviesList : Fragment() {

    private var listener: MoviesListFragmentClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_movies_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ConstraintLayout>(R.id.avengers_layout)
            .setOnClickListener { listener?.moveToMovieDetailsFragment() }
        view.findViewById<ImageView>(R.id.avengers_image).apply { setRoundedTopCorners(this) }
    }

    //communication with activity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MoviesListFragmentClickListener) listener = context
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface MoviesListFragmentClickListener {
        fun moveToMovieDetailsFragment()
    }

    private fun setRoundedTopCorners(image: ImageView?) {
        val curveRadius = 20F

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            image?.outlineProvider = object : ViewOutlineProvider() {
                @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                override fun getOutline(view: View?, outline: Outline?) {
                    outline?.setRoundRect(0, 0, view!!.width, (view.height+curveRadius).toInt(), curveRadius)
                }
            }
            image?.clipToOutline = true
        }
    }
}