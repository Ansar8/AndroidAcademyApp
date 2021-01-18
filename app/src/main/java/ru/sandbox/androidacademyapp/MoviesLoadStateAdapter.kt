package ru.sandbox.androidacademyapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView

class MoviesLoadStateAdapter(private val retry: () -> Unit) :
        LoadStateAdapter<MoviesLoadStateAdapter.MoviesLoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): MoviesLoadStateViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.movie_load_state_footer, parent, false)
        return MoviesLoadStateViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoviesLoadStateViewHolder, loadState: LoadState) {
        holder.onBind(loadState)
    }

    inner class MoviesLoadStateViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val progressBar: ProgressBar = itemView.findViewById(R.id.movies_footer_progress_bar)
        private val errorMessage: TextView = itemView.findViewById(R.id.movies_footer_error_msg)
        private val retryButton: Button = itemView.findViewById(R.id.movies_footer_retry_button)

        init {
            retryButton.setOnClickListener { retry.invoke() }
        }

        fun onBind(loadState: LoadState){
            progressBar.isVisible = loadState is LoadState.Loading
            retryButton.isVisible = loadState !is LoadState.Loading
            errorMessage.isVisible = loadState !is LoadState.Loading
        }
    }
}