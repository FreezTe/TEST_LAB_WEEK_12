package com.example.test_lab_week_12

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.example.test_lab_week_12.model.Movie
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private val movieAdapter = MovieAdapter(object : MovieAdapter.MovieClickListener {
        override fun onMovieClick(movie: Movie) {
            val intent = Intent(this@MainActivity, DetailsActivity::class.java)

            // Kirim field-field yang dibutuhkan DetailsActivity
            intent.putExtra(DetailsActivity.EXTRA_TITLE, movie.title)
            intent.putExtra(DetailsActivity.EXTRA_RELEASE, movie.releaseDate)
            intent.putExtra(DetailsActivity.EXTRA_POSTER, movie.posterPath)
            intent.putExtra(DetailsActivity.EXTRA_OVERVIEW, movie.overview)

            startActivity(intent)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.movie_list)
        recyclerView.adapter = movieAdapter

        val movieRepository = (application as MovieApplication).movieRepository

        val movieViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MovieViewModel(movieRepository) as T
                }
            }
        )[MovieViewModel::class.java]

        // Collect StateFlow
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                // collect list movie
                launch {
                    movieViewModel.popularMovies.collect { movies ->
                        movieAdapter.addMovies(movies)
                    }
                }

                // collect error
                launch {
                    movieViewModel.error.collect { error ->
                        if (error.isNotEmpty()) {
                            Snackbar
                                .make(recyclerView, error, Snackbar.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            }
        }
    }
}
