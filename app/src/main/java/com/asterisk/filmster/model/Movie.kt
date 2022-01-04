package com.asterisk.filmster.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import org.json.JSONArray

@Parcelize
data class Movie(
    val movieId: Int,
    val title: String,
    val overview: String,
    val voteAverage: Double,
    private val posterPath: String
): Parcelable {

    @IgnoredOnParcel
    val posterImageUrl = "https://image.tmdb.org/t/p/w342/$posterPath"

    companion object {
        fun fromJsonArray(movieJsonArray: JSONArray): List<Movie> {
            val movies = mutableListOf<Movie>()

            for (index in 0 until movieJsonArray.length()) {
                val movieJson = movieJsonArray.getJSONObject(index)
                movies.add(
                    Movie(
                        movieId = movieJson.getInt("id"),
                        voteAverage = movieJson.getDouble("vote_average"),
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