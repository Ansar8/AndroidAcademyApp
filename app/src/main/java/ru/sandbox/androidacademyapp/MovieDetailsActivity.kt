package ru.sandbox.androidacademyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class MovieDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        val textView: TextView = findViewById(R.id.back_text)
        textView.setOnClickListener { onBackPressed() }
    }
}