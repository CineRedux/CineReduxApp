package com.example.cineredux_v2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView


class MovieDetails : Fragment() {

    private lateinit var moviePoster: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var runtimeTextView: TextView
    private lateinit var yearTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_movie_details, container, false)

        // Initialize views
        moviePoster = view.findViewById(R.id.movie_poster)
        titleTextView = view.findViewById(R.id.title_text_view)
        runtimeTextView = view.findViewById(R.id.runtime_text_view)
        yearTextView = view.findViewById(R.id.year_text_view)

        // Set data (replace with actual data)
        setMovieDetails("Inception", "Runtime: 148 min", "Release year: 2010", R.drawable.inception_2010)

        return view
    }

    private fun setMovieDetails(title: String, runtime: String, year: String, posterResId: Int) {
        titleTextView.text = title
        runtimeTextView.text = runtime
        yearTextView.text = year
        moviePoster.setImageResource(posterResId)
    }
}
