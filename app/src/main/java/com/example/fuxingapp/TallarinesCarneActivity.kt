package com.example.fuxingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import android.content.Intent
import android.widget.Button
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import android.widget.Toast

class TallarinesCarneActivity : AppCompatActivity() {

    private var quantity: Int = 1
    private lateinit var textViewQuantity: TextView
    private lateinit var textViewPrice: TextView
    private lateinit var buttonOrderNow: Button
    private lateinit var buttonAddToCart: Button
    private val basePrice = 16.50 // Precio base del producto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tallarines_carne)

        val imageButtonBack = findViewById<ImageButton>(R.id.imageButtonBack)
        imageButtonBack.setOnClickListener {
            onBackPressed()
        }

        val imageButtonFavorite = findViewById<ImageButton>(R.id.imageButtonFavorite)
        imageButtonFavorite.setOnClickListener {
            toggleFavorite(imageButtonFavorite)
            updateFavoriteButtonState(imageButtonFavorite)
        }

        // Cargar y actualizar el estado inicial del favorito
        updateFavoriteButtonState(imageButtonFavorite)

        textViewQuantity = findViewById(R.id.textViewQuantity)
        val imageButtonRemoveQuantity: ImageButton = findViewById(R.id.imageButtonRemoveQuantity)
        val imageButtonAddQuantity: ImageButton = findViewById(R.id.imageButtonAddQuantity)
        textViewPrice = findViewById(R.id.textViewPrice)
        buttonOrderNow = findViewById(R.id.buttonOrderNow)
        buttonAddToCart = findViewById(R.id.buttonAddToCart)

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
            // Crear un CartItem para el producto actual con la cantidad seleccionada
            val item = CartItem(PRODUCT_ID, quantity, basePrice)
            // Añadir el item al carrito (CartManager maneja si ya existe)
            CartManager.addItemToCart(this, item)

            // Obtener todos los items del carrito
            val cartItems = CartManager.getCartItems(this)
            // Calcular el total de todos los items en el carrito
            val totalCartPrice = cartItems.sumOf { it.quantity * it.price }

            // Navegar a la actividad de método de pago, pasando el total del carrito
            val intent = Intent(this, MetodoPagoActivity::class.java)
            intent.putExtra("cart_total", totalCartPrice) // Usar la misma clave que el diálogo del carrito
            startActivity(intent)
        }

        // Listener para el botón Añadir al Carrito
        buttonAddToCart.setOnClickListener {
            val item = CartItem(PRODUCT_ID, quantity, basePrice)
            CartManager.addItemToCart(this, item)
            Toast.makeText(this, "Producto añadido al carrito", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        // Actualizar el estado del favorito cada vez que la actividad se vuelve visible
        val imageButtonFavorite = findViewById<ImageButton>(R.id.imageButtonFavorite)
        updateFavoriteButtonState(imageButtonFavorite)
    }

    private fun updateFavoriteButtonState(button: ImageButton) {
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val favoriteProductIds = prefs.getStringSet("favorite_product_ids", emptySet()) ?: emptySet()

        val isFavorite = PRODUCT_ID in favoriteProductIds

        if (isFavorite) {
            button.setImageResource(R.drawable.ic_heart_filled_placeholder)
            button.setColorFilter(ContextCompat.getColor(this, R.color.red_primary_placeholder))
            button.tag = "filled"
        } else {
            button.setImageResource(R.drawable.ic_heart_outline_placeholder)
            button.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray))
            button.tag = "outline"
        }
    }

    private fun updateQuantityText() {
        textViewQuantity.text = quantity.toString()
        val currentPrice = basePrice * quantity
        textViewPrice.text = String.format("S/.%.2f", currentPrice)
    }

    private fun toggleFavorite(button: ImageButton) {
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = prefs.edit()

        // Leer el Set actual de IDs favoritos
        val favoriteProductIds = prefs.getStringSet("favorite_product_ids", emptySet())?.toMutableSet() ?: mutableSetOf()

        val isFavorite = button.tag == "filled"

        if (isFavorite) {
            button.setImageResource(R.drawable.ic_heart_outline_placeholder)
            button.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray))
            button.tag = "outline"
            // Eliminar el PRODUCT_ID del Set
            favoriteProductIds.remove(PRODUCT_ID)
        } else {
            button.setImageResource(R.drawable.ic_heart_filled_placeholder)
            button.setColorFilter(ContextCompat.getColor(this, R.color.red_primary_placeholder))
            button.tag = "filled"
            // Añadir el PRODUCT_ID al Set
            favoriteProductIds.add(PRODUCT_ID)
        }

        // Guardar el Set actualizado de IDs favoritos
        editor.putStringSet("favorite_product_ids", favoriteProductIds)
        editor.apply()
    }

    companion object {
        private const val PRODUCT_ID = "tallarines_carne"
    }
} 