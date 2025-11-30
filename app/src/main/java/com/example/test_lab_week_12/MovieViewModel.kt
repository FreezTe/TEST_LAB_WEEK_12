package com.example.test_lab_week_12

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test_lab_week_12.MovieRepository
import com.example.test_lab_week_12.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.Calendar

class MovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    // ------- StateFlow pengganti LiveData -------
    private val _popularMovies = MutableStateFlow(emptyList<Movie>())
    val popularMovies: StateFlow<List<Movie>> = _popularMovies

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    init {
        fetchPopularMovies()
    }

    // ------- fetch dengan Flow + FILTER (ASSIGNMENT) -------
    private fun fetchPopularMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()

            movieRepository.fetchMovies()
                .catch { exception ->
                    _error.value = "An exception occurred: ${exception.message}"
                }
                .collect { movies ->
                    // di sini kita pasang FILTER + SORT
                    val filtered = movies
                        .filter { movie ->
                            movie.releaseDate?.startsWith(currentYear) == true
                        }
                        .sortedByDescending { it.popularity }

                    _popularMovies.value = filtered
                }
        }
    }
}
