package com.example.fuxingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import android.content.Intent
import android.widget.Button

class ChaufaCarneActivity : AppCompatActivity() {

    private var quantity: Int = 1
    private lateinit var textViewQuantity: TextView
    private lateinit var textViewPrice: TextView
    private lateinit var buttonOrderNow: Button
    private val basePrice = 16.00 // Precio base del producto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chaufa_carne)

        val imageButtonBack = findViewById<ImageButton>(R.id.imageButtonBack)
        imageButtonBack.setOnClickListener {
            onBackPressed()
        }

        val imageButtonFavorite = findViewById<ImageButton>(R.id.imageButtonFavorite)
        imageButtonFavorite.setOnClickListener {
            toggleFavorite(imageButtonFavorite)
        }

        textViewQuantity = findViewById(R.id.textViewQuantity)
        val imageButtonRemoveQuantity: ImageButton = findViewById(R.id.imageButtonRemoveQuantity)
        val imageButtonAddQuantity: ImageButton = findViewById(R.id.imageButtonAddQuantity)
        textViewPrice = findViewById(R.id.textViewPrice)
        buttonOrderNow = findViewById(R.id.buttonOrderNow)

        updateQuantityText() // Mostrar el precio inicial

        imageButtonRemoveQuantity.setOnClickListener {
            if (quantity > 1) {
                quantity--
                updateQuantityText()
            }
        }

        imageButtonAddQuantity.setOnClickListener {
            quantity++
            updateQuantityText()
        }

        buttonOrderNow.setOnClickListener {
            val totalPriceForProduct = basePrice * quantity
            val intent = Intent(this, MetodoPagoActivity::class.java)
            intent.putExtra("product_price", totalPriceForProduct)
            startActivity(intent)
        }
    }

    private fun updateQuantityText() {
        textViewQuantity.text = quantity.toString()
        val currentPrice = basePrice * quantity
        textViewPrice.text = String.format("S/.%.2f", currentPrice)
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