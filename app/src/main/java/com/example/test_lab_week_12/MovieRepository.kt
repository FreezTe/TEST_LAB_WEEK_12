package com.example.test_lab_week_12

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.test_lab_week_12.api.MovieService
import com.example.test_lab_week_12.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MovieRepository(private val movieService: MovieService) {
    private val apiKey = "3af5ad67c67b171e5ae26d0bf2677e31"

    // LiveData that contains a list of movies
    private val movieLiveData = MutableLiveData<List<Movie>>()
    // fetch movies from the API
// this function returns a Flow of Movie objects
// a Flow is a type of coroutine that can emit multiple values
// for more info, see: https://kotlinlang.org/docs/flow.html#flows
    fun fetchMovies(): Flow<List<Movie>> {
        return flow {
            // emit the list of popular movies from the API
            emit(movieService.getPopularMovies(apiKey).results)
            // use Dispatchers.IO to run this coroutine on a shared pool of
            //threads
        }.flowOn(Dispatchers.IO)
    }

}