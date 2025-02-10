package com.netflixclone.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.netflixclone.R

class ErrorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)

        val errorMessage = intent.getStringExtra("error_message") ?: "Something went wrong!"
        val textViewError = findViewById<TextView>(R.id.textViewError)
        val retryButton = findViewById<Button>(R.id.buttonRetry)

        textViewError.text = errorMessage

        retryButton.setOnClickListener {
            // Navigate back to main screen
            val intent = Intent(this@ErrorActivity, LoginActivity::class.java) // Change as needed
            startActivity(intent)
            finish()
        }
    }
}
