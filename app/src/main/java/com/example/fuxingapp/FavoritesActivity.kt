package com.example.fuxingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import android.content.Intent
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore

class FavoritesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var platosAdapter: PlatosAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var listaPlatosFavoritos: MutableList<Plato> = mutableListOf()
    private var favoritosIds: MutableSet<String> = mutableSetOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        // Botón de regreso
        val botonRegresar = findViewById<ImageButton>(R.id.imageButtonBack)
        botonRegresar.setOnClickListener {
            finish()
        }

        recyclerView = findViewById(R.id.recyclerViewFavorites)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        platosAdapter = PlatosAdapter(listaPlatosFavoritos, favoritosIds) { plato ->
            val intent = Intent(this, DetallePlatoActivity::class.java)
            intent.putExtra("plato", plato)
            startActivity(intent)
        }

        recyclerView.adapter = platosAdapter

        cargarFavoritos()
    }

    private fun cargarFavoritos() {
        val userId = auth.currentUser?.uid ?: return
        db.collection("Usuarios").document(userId).collection("Favoritos")
            .get()
            .addOnSuccessListener { documentos ->
                favoritosIds.clear()
                for (doc in documentos) {
                    favoritosIds.add(doc.id)
                }

                db.collection("Platos")
                    .whereIn(FieldPath.documentId(), favoritosIds.toList())
                    .get()
                    .addOnSuccessListener { platosDocs ->
                        listaPlatosFavoritos.clear()  // <-- mueve esto aquí
                        for (doc in platosDocs) {
                            val plato = doc.toObject(Plato::class.java)
                            plato.id = doc.id
                            listaPlatosFavoritos.add(plato)
                        }
                        platosAdapter.actualizarPlatos(listaPlatosFavoritos)
                    }
            }
    }

    override fun onResume() {
        super.onResume()
        cargarFavoritos()
    }
}
