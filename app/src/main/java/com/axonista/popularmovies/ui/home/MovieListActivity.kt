package com.axonista.popularmovies.ui.home


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.axonista.popularmovies.R
import com.axonista.popularmovies.databinding.ActivityMainBinding
import com.axonista.popularmovies.ui.adapter.CustomAdapterMovies
import com.axonista.popularmovies.ui.moviedetail.MovieDetailScrollingActivity
import com.axonista.popularmovies.util.*
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

@Keep
class MovieListActivity : AppCompatActivity(),HasAndroidInjector {

    companion object {
        const val ANIMATION_DURATION = 1000.toLong()
    }

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    val viewModel:  MovieViewModel by viewModels{
        viewModelFactory
    }

    private lateinit var dataBind: ActivityMainBinding
    private lateinit var customAdapterMovies: CustomAdapterMovies
    private lateinit var searchView: SearchView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBind = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupUI()
        initializeObserver()
        handleNetworkChanges()
        setupAPICall()
        callwithDummyData()
    }

    private fun callwithDummyData() {
        viewModel.getMovies()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    private fun setupUI() {
        customAdapterMovies = CustomAdapterMovies()
        dataBind.recyclerViewMovies.apply {
            layoutManager = GridLayoutManager(context,2)
            itemAnimator = DefaultItemAnimator()
            adapter = customAdapterMovies
            addOnItemTouchListener(
                RecyclerItemClickListener(
                    applicationContext,
                    object : RecyclerItemClickListener.OnItemClickListener {
                        override fun onItemClick(view: View, position: Int) {
                            if (customAdapterMovies.getData().isNotEmpty()) {
                                val searchItem = customAdapterMovies.getData()[position]
                                searchItem?.let {
                                    val intent =
                                        Intent(
                                            applicationContext,
                                            MovieDetailScrollingActivity::class.java
                                        )
                                    intent.putExtra(AppConstant.INTENT_POSTER, it.backdropPath)
                                    intent.putExtra(AppConstant.MOVIE_ID, it.id)
                                    intent.putExtra(AppConstant.MOVIE_TITLE, it.title)
                                    startActivity(intent)
                                }

                            }
                        }

                    })
            )
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                    val visibleItemCount = layoutManager!!.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    viewModel.checkForLoadMoreItems(
                        visibleItemCount,
                        totalItemCount,
                        firstVisibleItemPosition
                    )
                }

            })
        }
    }



    private fun initializeObserver() {
        viewModel.loadMoreListLiveData.observe(this, Observer {
            if (it) {
                customAdapterMovies.setData(null)
                Handler().postDelayed({
                    viewModel.loadMore()
                }, 2000)
            }
        })
    }

    private fun setupAPICall() {
        viewModel.moviesLiveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> {
                    dataBind.recyclerViewMovies.hide()
                    dataBind.linearLayoutSearch.hide()
                    dataBind.progressBar.show()
                }
                is State.Success -> {
                    dataBind.recyclerViewMovies.show()
                    dataBind.linearLayoutSearch.hide()
                    dataBind.progressBar.hide()
                    customAdapterMovies.setData(state.data)
                }
                is State.Error -> {
                    dataBind.progressBar.hide()
                    showToast(state.message)
                }
            }
        })

    }

    private fun handleNetworkChanges() {
        NetworkUtils.getNetworkLiveData(applicationContext).observe(this, Observer { isConnected ->
            if (!isConnected) {
                dataBind.textViewNetworkStatus.text = getString(R.string.text_no_connectivity)
                dataBind.networkStatusLayout.apply {
                    show()
                    setBackgroundColor(getColorRes(R.color.colorStatusNotConnected))
                }
            } else {
                if (viewModel.moviesLiveData.value is State.Error || customAdapterMovies.itemCount == 0) {
                    viewModel.getMovies()
                }
                dataBind.textViewNetworkStatus.text = getString(R.string.text_connectivity)
                dataBind.networkStatusLayout.apply {
                    setBackgroundColor(getColorRes(R.color.colorStatusConnected))
                    animate()
                        .alpha(1f)
                        .setStartDelay(ANIMATION_DURATION)
                        .setDuration(ANIMATION_DURATION)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                hide()
                            }
                        })
                }
            }
        })
    }


    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector as AndroidInjector<Any>
    }


}