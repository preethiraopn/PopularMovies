package com.axonista.popularmovies.ui.moviedetail


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.axonista.popularmovies.R
import com.axonista.popularmovies.databinding.ActivityMovieDetailScrollingBinding
import com.axonista.popularmovies.ui.home.MovieListActivity
import com.axonista.popularmovies.util.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_movie_detail_scrolling.*
import javax.inject.Inject

@Keep
class MovieDetailScrollingActivity : AppCompatActivity()  , HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private lateinit var dataBind: ActivityMovieDetailScrollingBinding


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: MovieDetailViewModel
    private var movieTitle = ""
    private var movieID: Int = 0
    private var moviePoster = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBind = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail_scrolling)
        setupViewModel()
        setSupportActionBar(toolbar)
        setupUI()
        handleNetworkChanges()
        setupAPICall()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setupUI() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        if (intent.hasExtra(AppConstant.MOVIE_ID) )
            movieID = intent.getIntExtra(AppConstant.MOVIE_ID,0)
        if (intent.hasExtra(AppConstant.MOVIE_TITLE) && intent.getStringExtra(AppConstant.MOVIE_TITLE) != null)
        movieTitle = intent.getStringExtra(AppConstant.MOVIE_TITLE) !!
        if (intent.hasExtra(AppConstant.INTENT_POSTER) && intent.getStringExtra(AppConstant.INTENT_POSTER) != null)
            moviePoster = intent.getStringExtra(AppConstant.INTENT_POSTER)!!
//        dataBind.tilte.text = movieTitle
        dataBind.detailLayout.tilte.text = movieTitle
        Glide.with(this).load(AppConstant.getImageUrl(moviePoster))
            .centerCrop()
            .thumbnail(0.5f)
            .placeholder(R.drawable.ic_launcher_background)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(dataBind.backgropPoster)

    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(MovieDetailViewModel::class.java)
    }

    /**
     * observing changes in movieDetail live data
     */
    @SuppressLint("SetTextI18n")
    private fun setupAPICall() {
        viewModel.movieDetailLiveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> {
                    dataBind.progressBar.show()
                    dataBind.cardViewMovieDetail.hide()
                }
                is State.Success -> {
                    dataBind.progressBar.hide()
                    dataBind.cardViewMovieDetail.show()
                    state.data?.let {
                        dataBind.detailLayout.releaseYear.text = resources.getString(R.string.releasedate) + it.releaseDate
                        dataBind.detailLayout.duration.text = viewModel.getHour(it.runtime)
                        dataBind.detailLayout.genre.text = it.genres?.let { listGenres ->
                            viewModel.getGenre(
                                listGenres
                            )
                        }
                        dataBind.detailLayout.progressView.setProgress((it.voteAverage * 10).toInt(),false)
                        dataBind.overviewText.text = it.overview
                        it.posterPath?.let { posterPath->
                            Glide.with(this).load(AppConstant.getImageUrl(posterPath))
                                .centerCrop()
                                .thumbnail(0.5f)
                                .placeholder(R.drawable.ic_launcher_background)
                                .centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(dataBind.detailLayout.imagePoster)
                         }

                    }
                }
                is State.Error -> {
                    dataBind.progressBar.hide()
                    dataBind.cardViewMovieDetail.hide()
                    showToast(state.message)
                }
            }
        })
        getMoviesDetail(movieID)
    }


    /**
     * handling offline online functionality
     */
    private fun handleNetworkChanges() {
        NetworkUtils.getNetworkLiveData(applicationContext).observe(this, Observer { isConnected ->
            if (!isConnected) {
                dataBind.textViewNetworkStatus.text = getString(R.string.text_no_connectivity)
                dataBind.networkStatusLayout.apply {
                    show()
                    setBackgroundColor(getColorRes(R.color.colorStatusNotConnected))
                }
            } else {
                if (viewModel.movieDetailLiveData.value is State.Error) {
                    getMoviesDetail(movieID)
                }
                dataBind.textViewNetworkStatus.text = getString(R.string.text_connectivity)
                dataBind.networkStatusLayout.apply {
                    setBackgroundColor(getColorRes(R.color.colorStatusConnected))
                    animate()
                        .alpha(1f)
                        .setStartDelay(MovieListActivity.ANIMATION_DURATION)
                        .setDuration(MovieListActivity.ANIMATION_DURATION)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                hide()
                            }
                        })
                }
            }
        })
    }

    private fun getMoviesDetail(movieID: Int) {
        viewModel.getMovieDetail(movieID)
    }
    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector as AndroidInjector<Any>
    }


}
