package com.example.fuxingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View // Importante para View.VISIBLE y View.GONE
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.bottomnavigation.BottomNavigationView
// Asegúrate de que todos los imports necesarios estén presentes.
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import android.util.Log
import android.content.Context // Importar Context
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var cardViewFood1: MaterialCardView
    private lateinit var cardViewFood2: MaterialCardView
    private lateinit var cardViewFood3: MaterialCardView
    private lateinit var cardViewFood4: MaterialCardView

    private lateinit var allFoodCards: List<MaterialCardView>

    // Mapa para relacionar IDs de botones de favorito con IDs de producto
    private val favoriteButtonProductMap = mapOf(
        R.id.imageButtonFavorite1 to "chaufa_langostinos",
        R.id.imageButtonFavorite2 to "chaufa_carne",
        R.id.imageButtonFavorite3 to "tallarines_pollo",
        R.id.imageButtonFavorite4 to "tallarines_carne"
    )

    // Define constantes para las categorías. Estas deben coincidir con el texto de tus Chips.
    private val CATEGORY_TODO = "Todo"
    private val CATEGORY_CHAUFAS = "Chaufas"
    private val CATEGORY_AEROPUERTOS = "Aeropuertos"
    private val CATEGORY_TALLARINES = "Tallarines"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializa las vistas de las tarjetas de comida
        cardViewFood1 = findViewById(R.id.cardViewFood1)
        cardViewFood2 = findViewById(R.id.cardViewFood2)
        cardViewFood3 = findViewById(R.id.cardViewFood3)
        cardViewFood4 = findViewById(R.id.cardViewFood4)

        // Asigna etiquetas (tags) de categoría a cada tarjeta de comida
        // cardViewFood1 y cardViewFood2 son Chaufas
        cardViewFood1.tag = CATEGORY_CHAUFAS // Chaufa Langostinos
        cardViewFood2.tag = CATEGORY_CHAUFAS // Chaufa Carne

        // cardViewFood3 y cardViewFood4 son Tallarines
        cardViewFood3.tag = CATEGORY_TALLARINES // Tallarines Pollo
        cardViewFood4.tag = CATEGORY_TALLARINES // Tallarines Carne

        // Crea una lista de todas las tarjetas para facilitar el filtrado
        allFoodCards = listOf(cardViewFood1, cardViewFood2, cardViewFood3, cardViewFood4)

        // Set click listeners for product cards
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

        // Cargar y actualizar el estado de los favoritos al iniciar la actividad
        updateFavoriteButtonState(imageButtonFavorite1)
        updateFavoriteButtonState(imageButtonFavorite2)
        updateFavoriteButtonState(imageButtonFavorite3)
        updateFavoriteButtonState(imageButtonFavorite4)

        // --- Lógica de Filtrado ---
        val chipGroupCategories = findViewById<ChipGroup>(R.id.chipGroupCategories)

        chipGroupCategories.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                val checkedChipId = checkedIds[0] // singleSelection es true
                val selectedChip = group.findViewById<Chip>(checkedChipId)
                if (selectedChip != null) {
                    filterFoodItems(selectedChip.text.toString())
                    updateChipAppearance(group, selectedChip.id)
                }
            } else {
                // Si por alguna razón ningún chip está seleccionado (poco probable con singleSelection="true")
                // podrías volver a seleccionar "Todo" por defecto.
                val chipTodo = findViewById<Chip>(R.id.chipTodo)
                if (!chipTodo.isChecked) {
                    chipTodo.isChecked = true // Esto disparará el listener de nuevo
                } else {
                    // Si ya está "Todo" y checkedIds está vacío, simplemente filtra y actualiza apariencia
                    filterFoodItems(CATEGORY_TODO)
                    updateChipAppearance(group, R.id.chipTodo)
                }
            }
        }

        // Configuración inicial del filtro y apariencia de chips
        // El chip "Todo" está marcado como `android:checked="true"` en tu XML.
        // Nos aseguramos de que el filtro inicial y la apariencia se apliquen correctamente.
        val initialCheckedId = chipGroupCategories.checkedChipId
        if (initialCheckedId != View.NO_ID) {
            val initialChip = findViewById<Chip>(initialCheckedId)
            filterFoodItems(initialChip.text.toString()) // Filtra por "Todo"
            updateChipAppearance(chipGroupCategories, initialCheckedId) // Establece la apariencia inicial
        } else {
            // Como fallback, si ningún chip estuviera chequeado inicialmente (lo cual no debería pasar con tu XML)
            findViewById<Chip>(R.id.chipTodo).isChecked = true
            // La línea anterior disparará el listener, que llamará a filterFoodItems y updateChipAppearance.
        }


        // Get reference to the BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // Set item selected listener for the BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener { item ->
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
                R.id.navigation_favorites -> {
                    val intent = Intent(this, FavoritesActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_cart -> {
                    // Anteriormente mostraba el diálogo del carrito aquí.
                    // Ahora esta acción se ha movido al listener del FAB.
                    // Puedes decidir si este ítem debe hacer algo más o estar inactivo.
                    // Por ahora, lo dejamos inactivo (retornar true para que no se propague el evento)
                    true
                }
                else -> false
            }
        }

        // Get reference to the FAB
        val fabCart = findViewById<FloatingActionButton>(R.id.fabCart)

        // Set click listener for the FAB to show the cart dialog
        fabCart.setOnClickListener {
            val cartDialog = CartDialogFragment()
            cartDialog.show(supportFragmentManager, "CartDialogFragment")
        }
    }

    override fun onResume() {
        super.onResume()
        // Actualizar el estado de los favoritos cada vez que la actividad se vuelve visible
        updateFavoriteButtonState(findViewById(R.id.imageButtonFavorite1))
        updateFavoriteButtonState(findViewById(R.id.imageButtonFavorite2))
        updateFavoriteButtonState(findViewById(R.id.imageButtonFavorite3))
        updateFavoriteButtonState(findViewById(R.id.imageButtonFavorite4))
    }

    private fun updateFavoriteButtonState(button: ImageButton) {
        // Obtiene el ID del producto del mapa usando el ID del botón
        val productId = favoriteButtonProductMap[button.id] ?: return

        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val favoriteProductIds = prefs.getStringSet("favorite_product_ids", emptySet()) ?: emptySet()

        val isFavorite = productId in favoriteProductIds

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

    private fun filterFoodItems(selectedCategory: String) {
        for (card in allFoodCards) {
            val itemCategory = card.tag as? String // Obtiene la categoría de la etiqueta

            when (selectedCategory) {
                CATEGORY_TODO -> card.visibility = View.VISIBLE // Muestra todas si "Todo" está seleccionado
                itemCategory -> card.visibility = View.VISIBLE  // Muestra si la categoría del item coincide
                else -> card.visibility = View.GONE             // Oculta si no coincide
            }
        }
        // Si una categoría como "Aeropuertos" es seleccionada y no hay items con esa etiqueta,
        // todas las tarjetas no coincidentes se ocultarán, lo cual es el comportamiento esperado.
    }

    private fun updateChipAppearance(chipGroup: ChipGroup, selectedChipId: Int) {
        for (i in 0 until chipGroup.childCount) {
            val child = chipGroup.getChildAt(i)
            if (child is Chip) {
                if (child.id == selectedChipId) {
                    // Chip seleccionado: fondo rojo, texto blanco
                    child.setChipBackgroundColorResource(R.color.red_primary_placeholder)
                    child.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                } else {
                    // Otros chips: fondo gris, texto negro
                    child.setChipBackgroundColorResource(R.color.light_grey_placeholder)
                    child.setTextColor(ContextCompat.getColor(this, android.R.color.black))
                }
            }
        }
    }

    private fun toggleFavorite(button: ImageButton) {
        // Obtiene el ID del producto del mapa usando el ID del botón
        val productId = favoriteButtonProductMap[button.id] ?: return

        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = prefs.edit()

        // Leer el Set actual de IDs favoritos
        val favoriteProductIds = prefs.getStringSet("favorite_product_ids", emptySet())?.toMutableSet() ?: mutableSetOf()

        // Determinar el nuevo estado de favorito basado en si estaba en el Set
        val isCurrentlyFavorite = productId in favoriteProductIds

        if (isCurrentlyFavorite) {
            // Si actualmente es favorito, desmarcarlo
            button.setImageResource(R.drawable.ic_heart_outline_placeholder)
            button.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray))
            button.tag = "outline" // Actualizar el tag visualmente (opcional pero consistente)
            // Eliminar el productId del Set
            favoriteProductIds.remove(productId)
        } else {
            // Si actualmente no es favorito, marcarlo
            button.setImageResource(R.drawable.ic_heart_filled_placeholder)
            button.setColorFilter(ContextCompat.getColor(this, R.color.red_primary_placeholder)) // Usa tu color rojo primario
            button.tag = "filled" // Actualizar el tag visualmente (opcional pero consistente)
            // Añadir el productId al Set
            favoriteProductIds.add(productId)
        }

        // Guardar el Set actualizado de IDs favoritos
        editor.putStringSet("favorite_product_ids", favoriteProductIds)
        editor.apply() // Aplica los cambios de forma asíncrona

        // Llama a updateFavoriteButtonState para asegurar que el ícono se actualice si el tag visual no lo hizo (aunque con los cambios de arriba no debería ser necesario)
        updateFavoriteButtonState(button)
    }
}