package com.example.fuxingapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fuxingapp.util.CartManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import com.example.fuxingapp.adapter.CartItemAdapter
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchEditText: EditText
    private lateinit var chipGroupCategorias: ChipGroup
    private lateinit var platosAdapter: PlatosAdapter
    private var platosList: MutableList<Plato> = mutableListOf()
    private var favoritosSet: MutableSet<String> = mutableSetOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        recyclerView = findViewById(R.id.recyclerView)
        searchEditText = findViewById(R.id.searchEditText)
        chipGroupCategorias = findViewById(R.id.chipGroupCategories)

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        platosAdapter = PlatosAdapter(platosList, favoritosSet) { plato ->
            toggleFavorito(plato)
        }
        recyclerView.adapter = platosAdapter

        cargarFavoritos()
        cargarPlatos()
        setupBuscador()
        setupChips()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Ya estás en Home, no hacer nada
                    true
                }
                R.id.navigation_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                R.id.navigation_mood -> { // Este es el botón de Comentarios
                    startActivity(Intent(this, CustomerServiceActivity::class.java))
                    true
                }



                R.id.navigation_favorites -> {
                    startActivity(Intent(this, FavoritesActivity::class.java))
                    true
                }
                else -> false
            }
        }
        val buttonCart = findViewById<ImageButton>(R.id.buttonCart)
        buttonCart.setOnClickListener {
            mostrarDialogoCarrito()
        }

    }

    private fun cargarFavoritos() {
        val userId = auth.currentUser?.uid ?: return
        db.collection("Usuarios").document(userId).collection("Favoritos")
            .get()
            .addOnSuccessListener { snapshot ->
                favoritosSet.clear()
                for (doc in snapshot) {
                    favoritosSet.add(doc.id)
                }
                platosAdapter.notifyDataSetChanged()
            }
    }

    private fun cargarPlatos() {
        db.collection("Platos")
            .get()
            .addOnSuccessListener { result ->
                platosList.clear()
                val nuevosPlatos = mutableListOf<Plato>()
                for (document in result) {
                    val plato = document.toObject(Plato::class.java)
                    plato.id = document.id
                    nuevosPlatos.add(plato)
                }
                platosList = nuevosPlatos
                platosAdapter.actualizarPlatos(platosList)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al cargar platos", Toast.LENGTH_SHORT).show()
            }
    }

    private fun toggleFavorito(plato: Plato) {
        val userId = auth.currentUser?.uid ?: return
        val favRef = db.collection("Usuarios").document(userId)
            .collection("Favoritos").document(plato.id)

        if (favoritosSet.contains(plato.id)) {
            favRef.delete()
            favoritosSet.remove(plato.id)
        } else {
            favRef.set(hashMapOf("nombre" to plato.nombre))
            favoritosSet.add(plato.id)
        }

        platosAdapter.notifyDataSetChanged()
    }

    private fun setupBuscador() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                platosAdapter.filterByNombre(text.toString().lowercase().trim())
            }
        })
    }

    private fun setupChips() {
        val categorias = listOf("Todo", "Chaufas", "Aeropuertos", "Tallarines")
        chipGroupCategorias.removeAllViews()
        for (cat in categorias) {
            val chip = Chip(this).apply {
                text = cat
                isCheckable = true
                isClickable = true
            }
            chip.setOnClickListener {
                val filtro = if (cat == "Todo") null else cat
                platosAdapter.filterByCategoria(filtro)
            }
            chipGroupCategorias.addView(chip)
        }
    }

    private fun mostrarDialogoCarrito() {
        val cartItems = CartManager.obtenerItems().toMutableList()

        if (cartItems.isEmpty()) {
            Toast.makeText(this, "No hay productos en el carrito", Toast.LENGTH_SHORT).show()
            return
        }

        val dialogView = layoutInflater.inflate(R.layout.dialog_cart, null)
        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.recyclerViewCartItems)
        val totalText = dialogView.findViewById<TextView>(R.id.textViewTotalPrice)
        val btnCerrar = dialogView.findViewById<Button>(R.id.buttonCloseCart)
        val btnPagar = dialogView.findViewById<Button>(R.id.buttonProceedToPayment)

        val adapter = CartItemAdapter(cartItems, {
            totalText.text = "Total: S/. %.2f".format(CartManager.calcularTotal())
            if (CartManager.estaVacio()) {
                totalText.text = "Total: S/. 0.00"
                Toast.makeText(this, "Carrito vacío", Toast.LENGTH_SHORT).show()
            }
        }, {
            totalText.text = "Total: S/. %.2f".format(CartManager.calcularTotal())
            if (CartManager.estaVacio()) {
                totalText.text = "Total: S/. 0.00"
                Toast.makeText(this, "Carrito vacío", Toast.LENGTH_SHORT).show()
            }
        })

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        totalText.text = "Total: S/. %.2f".format(CartManager.calcularTotal())

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        btnCerrar.setOnClickListener { dialog.dismiss() }

        btnPagar.setOnClickListener {
            Toast.makeText(this, "Función de pago aún no implementada", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }

}
