package com.asterisk.filmster.model

import org.json.JSONArray

data class Movie(
    val movieId: Int,
    val title: String,
    val overview: String,
    private val posterPath: String
) {

    val posterImageUrl = "https://image.tmdb.org/t/p/w342/$posterPath"

    companion object {
        fun fromJsonArray(movieJsonArray: JSONArray): List<Movie> {
            val movies = mutableListOf<Movie>()

            for (index in 0 until movieJsonArray.length()) {
                val movieJson = movieJsonArray.getJSONObject(index)
                movies.add(
                    Movie(
                        movieId = movieJson.getInt("id"),
                        title = movieJson.getString("title"),
                        overview = movieJson.getString("overview"),
                        posterPath = movieJson.getString("poster_path")
                    )
                )
            }

            return movies
        }
    }
}