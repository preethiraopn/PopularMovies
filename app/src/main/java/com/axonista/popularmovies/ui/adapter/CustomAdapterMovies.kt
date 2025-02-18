package com.axonista.popularmovies.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.axonista.popularmovies.R
import com.axonista.popularmovies.data.model.MovieList
import com.axonista.popularmovies.ui.customviews.CircularProgressView
import com.axonista.popularmovies.util.AppConstant
import com.axonista.popularmovies.util.show


class CustomAdapterMovies :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }

    private var moviesList = ArrayList<MovieList.Movie?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == VIEW_TYPE_ITEM) {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_movie, parent, false)
            MovieViewHolder(view)
        } else {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_lazy_loading, parent, false)
            LoadingViewHolder(view)
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is MovieViewHolder) {
            if (moviesList[position] != null) {
                holder.bindItems(moviesList[position]!!)
            }
        } else if (holder is LoadingViewHolder) {
            holder.showLoadingView()
        }
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (moviesList[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    fun setData(newMoviesList: ArrayList<MovieList.Movie?>?) {
        if (newMoviesList != null) {
            if (moviesList.isNotEmpty())
                moviesList.removeAt(moviesList.size - 1)
            moviesList.clear()
            moviesList.addAll(newMoviesList)
        } else {
            moviesList.add(newMoviesList)
        }
        notifyDataSetChanged()
    }

    fun getData() = moviesList

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imagePoster: ImageView = itemView.findViewById(R.id.image_poster)
        private val textTitle: TextView = itemView.findViewById(R.id.movie_title)
        private val textYear: TextView = itemView.findViewById(R.id.movie_year)
        private val mProgressBar :CircularProgressView = itemView.findViewById(R.id.progress_view)

        @SuppressLint("SetTextI18n")
        fun bindItems(movie: MovieList.Movie) {
            textTitle.text = movie.title
            textYear.text = movie.releaseDate
            mProgressBar.setProgress((movie.voteAverage * 10).toInt(),false)
            Glide.with(imagePoster.context).load(AppConstant.getImageUrl(movie.posterPath))
                .centerCrop()
                .thumbnail(0.25f)
                .placeholder(R.drawable.ic_launcher_background)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imagePoster)
        }

    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)

        fun showLoadingView() {
            progressBar.show()
        }
    }

}