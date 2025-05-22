package com.example.fuxingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class CustomerServiceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_service)

        // Aquí puedes añadir la lógica para la interfaz de chat

        // Get reference to the back button
        val imageButtonBack: ImageButton = findViewById(R.id.imageButtonBack)

        // Set click listener for the back button
        imageButtonBack.setOnClickListener { 
            onBackPressed()
        }
    }
} 