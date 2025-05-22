package com.example.fuxingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.ImageButton
import com.google.android.material.card.MaterialCardView
import androidx.core.content.ContextCompat
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.view.MenuItem

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get references to the product cards
        val cardViewFood1 = findViewById<MaterialCardView>(R.id.cardViewFood1)
        val cardViewFood2 = findViewById<MaterialCardView>(R.id.cardViewFood2)
        val cardViewFood3 = findViewById<MaterialCardView>(R.id.cardViewFood3)
        val cardViewFood4 = findViewById<MaterialCardView>(R.id.cardViewFood4)

        // Set click listeners for product cards to navigate to their respective activities
        cardViewFood1.setOnClickListener { val intent = Intent(this, ChaufaLangostinosActivity::class.java); startActivity(intent) }
        cardViewFood2.setOnClickListener { val intent = Intent(this, ChaufaCarneActivity::class.java); startActivity(intent) }
        cardViewFood3.setOnClickListener { val intent = Intent(this, TallarinesPolloActivity::class.java); startActivity(intent) }
        cardViewFood4.setOnClickListener { val intent = Intent(this, TallarinesCarneActivity::class.java); startActivity(intent) }

        // Get references to the favorite buttons
        val imageButtonFavorite1 = findViewById<ImageButton>(R.id.imageButtonFavorite1)
        val imageButtonFavorite2 = findViewById<ImageButton>(R.id.imageButtonFavorite2)
        val imageButtonFavorite3 = findViewById<ImageButton>(R.id.imageButtonFavorite3)
        val imageButtonFavorite4 = findViewById<ImageButton>(R.id.imageButtonFavorite4)

        // Set click listeners for favorite buttons to toggle state
        imageButtonFavorite1.setOnClickListener { toggleFavorite(imageButtonFavorite1) }
        imageButtonFavorite2.setOnClickListener { toggleFavorite(imageButtonFavorite2) }
        imageButtonFavorite3.setOnClickListener { toggleFavorite(imageButtonFavorite3) }
        imageButtonFavorite4.setOnClickListener { toggleFavorite(imageButtonFavorite4) }

        // Get reference to the BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // Set item selected listener for the BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener {
            item ->
            when(item.itemId) {
                R.id.navigation_home -> {
                    // Already on the home screen, do nothing or refresh
                    true
                }
                R.id.navigation_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_mood -> {
                    val intent = Intent(this, CustomerServiceActivity::class.java)
                    startActivity(intent)
                    true
                }
                // Add cases for other menu items as needed
                else -> false
            }
        }
    }

    private fun toggleFavorite(button: ImageButton) {
        if (button.tag == "filled") {
            button.setImageResource(R.drawable.ic_heart_outline_placeholder)
            button.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray))
            button.tag = "outline"
        } else {
            button.setImageResource(R.drawable.ic_heart_filled_placeholder)
            button.setColorFilter(ContextCompat.getColor(this, R.color.red_primary_placeholder))
            button.tag = "filled"
        }
    }
}