package com.example.test_lab_week_12

import com.example.test_lab_week_12.api.MovieService
import com.example.test_lab_week_12.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MovieRepository(private val movieService: MovieService) {

    // Ganti dengan API key kamu
    private val apiKey = "3af5ad67c67b171e5ae26d0bf2677e31"

    // Versi Flow
    fun fetchMovies(): Flow<List<Movie>> {
        return flow {
            val response = movieService.getPopularMovies(apiKey)
            emit(response.results)
        }.flowOn(Dispatchers.IO)
    }
}
