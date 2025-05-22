package com.example.fuxingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.ImageButton

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Aquí puedes añadir la lógica para manejar los elementos de UI

        // Get reference to the logout button
        val buttonLogout: Button = findViewById(R.id.buttonLogout)

        // Set click listener for the logout button
        buttonLogout.setOnClickListener { 
            // Create an Intent to navigate to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            // Add flags to clear the activity stack so the user cannot go back to ProfileActivity
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            // Finish the current activity (ProfileActivity)
            finish()
        }

        // Get reference to the back button
        val imageButtonBack: ImageButton = findViewById(R.id.imageButtonBack)

        // Set click listener for the back button
        imageButtonBack.setOnClickListener { 
            onBackPressed()
        }
    }
} 