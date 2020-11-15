package ru.sandbox.androidacademyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navigationPage: ConstraintLayout = findViewById(R.id.navigation_page)
        navigationPage.setOnClickListener { moveToMovieDetails() }
    }

    private fun moveToMovieDetails() {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        startActivity(intent)
    }
}