package com.asterisk.filmster.ui

import android.os.Bundle
import android.util.Log
import android.widget.RatingBar
import android.widget.TextView
import com.asterisk.filmster.R
import com.asterisk.filmster.model.Movie
import com.asterisk.filmster.other.Constants.MOVIE_EXTRA
import com.asterisk.filmster.other.Constants.TRAILERS_URL
import com.asterisk.filmster.other.Constants.YOUTUBE_API_KEY
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import okhttp3.Headers


private const val TAG = "DetailActivity"

class DetailActivity : YouTubeBaseActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvOverview: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var ytPlayer: YouTubePlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        tvTitle = findViewById(R.id.tv_title)
        tvOverview = findViewById(R.id.tv_overview)
        ratingBar = findViewById(R.id.rb_rate)
        ytPlayer = findViewById(R.id.player)

        val movie = intent.getParcelableExtra<Movie>(MOVIE_EXTRA) as Movie

        tvTitle.text = movie.title
        tvOverview.text = movie.overview
        ratingBar.rating = movie.voteAverage.toFloat()

        val client = AsyncHttpClient()
        client.get(TRAILERS_URL.format(movie.movieId), object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "onFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                val results = json.jsonObject.getJSONArray("results")
                if (results.length() == 0) {
                    return
                }

                val movieTrailerJson = results.getJSONObject(0)
                val youTubeKey = movieTrailerJson.getString("key")
                initializeYoutubePlayer(youTubeKey)
            }

        })

    }

    private fun initializeYoutubePlayer(youTubeKey: String) {
        ytPlayer.initialize(YOUTUBE_API_KEY, object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                p0: YouTubePlayer.Provider?,
                p1: YouTubePlayer?,
                p2: Boolean
            ) {
                p1?.cueVideo(youTubeKey)
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                TODO("Not yet implemented")
            }

        })
    }
}