package com.example.fuxingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.preference.PreferenceManager
import android.util.Log
import android.widget.ImageButton
import androidx.recyclerview.widget.GridLayoutManager
import android.content.Intent
import android.widget.ImageView
import com.google.android.material.card.MaterialCardView

class FavoritesActivity : AppCompatActivity() {

    private lateinit var recyclerViewFavorites: RecyclerView
    private lateinit var favoritesAdapter: FavoritesAdapter // Necesitaremos crear este Adapter
    private val favoriteProductIds = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        val imageButtonBack = findViewById<ImageButton>(R.id.imageButtonBack)
        imageButtonBack.setOnClickListener {
            onBackPressed()
        }

        recyclerViewFavorites = findViewById(R.id.recyclerViewFavorites)
        recyclerViewFavorites.layoutManager = GridLayoutManager(this, 2)

        // Cargar IDs de productos favoritos desde SharedPreferences
        loadFavoriteProductIds()

        // Aquí necesitaríamos obtener los detalles de los productos favoritos
        // y pasárselos al adapter.
        val favoriteProducts = getFavoriteProducts(favoriteProductIds)

        favoritesAdapter = FavoritesAdapter(favoriteProducts) { product ->
            // Acción al hacer clic en un item
            val intent = when (product.id) {
                "chaufa_langostinos" -> Intent(this, ChaufaLangostinosActivity::class.java)
                "chaufa_carne" -> Intent(this, ChaufaCarneActivity::class.java)
                "tallarines_pollo" -> Intent(this, TallarinesPolloActivity::class.java)
                "tallarines_carne" -> Intent(this, TallarinesCarneActivity::class.java)
                else -> null // No hacer nada si el ID no coincide con ninguna actividad conocida
            }
            intent?.let { startActivity(it) } // Iniciar la actividad si el intent no es nulo
        }
        recyclerViewFavorites.adapter = favoritesAdapter
    }

    private fun loadFavoriteProductIds() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        // Leer el Set de IDs favoritos
        val favoriteIdsSet = prefs.getStringSet("favorite_product_ids", emptySet()) ?: emptySet()
        // Limpiar la lista actual y añadir los IDs cargados
        favoriteProductIds.clear()
        favoriteProductIds.addAll(favoriteIdsSet)

        // Eliminar logs de depuración si ya no son necesarios
        // Log.d("FavoritesActivity", "Favorite product IDs loaded: $favoriteProductIds")
    }

    private fun getFavoriteProducts(productIds: List<String>): List<Product> {
        // Esta es una función placeholder. Necesitarás implementar la lógica
        // para obtener los detalles de los productos basándose en sus IDs.
        // Podrías tener una lista o mapa predefinido de productos.
        val allProducts = listOf(
            Product("chaufa_langostinos", "Chaufa Con Langostinos", "Un sabroso arroz frito preparado con langostinos...", R.drawable.chaufa_langostinos_placeholder),
            Product("chaufa_carne", "Chaufa Con Carne", "Un plato emblemático que fusiona lo mejor...", R.drawable.chaufa_carne_placeholder),
            Product("tallarines_pollo", "Tallarines Con Pollo", "Clásico de la cocina donde se combinan fideos...", R.drawable.tallarin_pollo_placeholder),
            Product("tallarines_carne", "Tallarines Con Carne", "Es un delicioso plato preparado con fideos...", R.drawable.tallarin_carne_placeholder)
            // Añade todos tus productos aquí con sus IDs, nombres, descripciones y drawables
        )

        return allProducts.filter { it.id in productIds }
    }

    // Clase de datos simple para representar un producto (necesitarás crear este archivo)
    // data class Product(val id: String, val name: String, val description: String, val imageResId: Int)

    // Adapter para el RecyclerView (necesitarás crear este archivo)
    // class FavoritesAdapter(private val products: List<Product>) : RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {...}
} 