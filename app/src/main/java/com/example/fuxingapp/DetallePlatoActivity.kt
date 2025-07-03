package com.example.fuxingapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.fuxingapp.model.CartItem
import com.example.fuxingapp.util.CartManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DetallePlatoActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var plato: Plato
    private var cantidad = 1
    private var esFavorito = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_plato)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val platoIntent = intent.getSerializableExtra("plato") as? Plato
        if (platoIntent == null) {
            finish()
            return
        }
        plato = platoIntent

        val imageViewProduct = findViewById<ImageView>(R.id.imageViewProduct)
        val imageButtonBack = findViewById<ImageButton>(R.id.imageButtonBack)
        val imageButtonFavorite = findViewById<ImageButton>(R.id.imageButtonFavorite)
        val textViewProductName = findViewById<TextView>(R.id.textViewProductName)
        val textViewDescription = findViewById<TextView>(R.id.textViewDescription)
        val textViewRatingInfo = findViewById<TextView>(R.id.textViewRatingInfo)
        val ratingBar = findViewById<RatingBar>(R.id.ratingBarDetalle)
        val textViewPrice = findViewById<TextView>(R.id.textViewPrice)
        val textViewQuantity = findViewById<TextView>(R.id.textViewQuantity)
        val buttonAdd = findViewById<ImageButton>(R.id.imageButtonAddQuantity)
        val buttonRemove = findViewById<ImageButton>(R.id.imageButtonRemoveQuantity)
        val buttonAddToCart = findViewById<Button>(R.id.buttonAddToCart)

        textViewProductName.text = plato.nombre
        textViewDescription.text = plato.descripcion
        textViewPrice.text = "S/. ${"%.2f".format(plato.precio)}"
        ratingBar.rating = plato.rating
        textViewRatingInfo.text = "${plato.rating} - 20 mins"

        Glide.with(this)
            .load(plato.imagenUrl)
            .into(imageViewProduct)

        val userId = auth.currentUser?.uid ?: ""
        val favRef = db.collection("Usuarios").document(userId).collection("Favoritos").document(plato.id)
        favRef.get().addOnSuccessListener {
            esFavorito = it.exists()
            imageButtonFavorite.setImageResource(
                if (esFavorito) R.drawable.ic_heart_filled_placeholder else R.drawable.ic_heart_outline_placeholder
            )
            val color = if (esFavorito) getColor(R.color.red_primary_placeholder) else getColor(android.R.color.darker_gray)
            imageButtonFavorite.setColorFilter(color)
        }

        imageButtonFavorite.setOnClickListener {
            if (userId.isEmpty()) return@setOnClickListener
            esFavorito = !esFavorito
            if (esFavorito) {
                favRef.set(mapOf("nombre" to plato.nombre))
            } else {
                favRef.delete()
            }
            imageButtonFavorite.setImageResource(
                if (esFavorito) R.drawable.ic_heart_filled_placeholder else R.drawable.ic_heart_outline_placeholder
            )
            val color = if (esFavorito) getColor(R.color.red_primary_placeholder) else getColor(android.R.color.darker_gray)
            imageButtonFavorite.setColorFilter(color)
        }

        imageButtonBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        textViewQuantity.text = cantidad.toString()
        buttonAdd.setOnClickListener {
            cantidad++
            textViewQuantity.text = cantidad.toString()
        }
        buttonRemove.setOnClickListener {
            if (cantidad > 1) {
                cantidad--
                textViewQuantity.text = cantidad.toString()
            }
        }

        // Botón Agregar al Carrito
        buttonAddToCart.setOnClickListener {
            val item = CartItem(
                id = plato.id,
                nombre = plato.nombre,
                precioUnitario = plato.precio,
                cantidad = cantidad,
                imagenUrl = plato.imagenUrl
            )
            CartManager.agregarAlCarrito(item)
            Toast.makeText(this, "Agregado al carrito", Toast.LENGTH_SHORT).show()
        }

        val buttonOrdenarAhora = findViewById<Button>(R.id.buttonOrderNow)
        buttonOrdenarAhora.setOnClickListener {
            val item = CartItem(
                id = plato.id,
                nombre = plato.nombre,
                precioUnitario = plato.precio,
                cantidad = cantidad,
                imagenUrl = plato.imagenUrl
            )
            CartManager.vaciarCarrito() // Opcional: vacía primero si no quieres duplicar
            CartManager.agregarAlCarrito(item)

            val intent = Intent(this, MetodoPagoActivity::class.java)
            startActivity(intent)
        }

    }
}
