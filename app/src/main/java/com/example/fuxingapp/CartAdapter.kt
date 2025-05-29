package com.example.fuxingapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fuxingapp.CartItem
import android.widget.ImageButton

class CartAdapter(
    private val cartItems: List<CartItem>,
    private val onRemoveItemClick: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewItemName: TextView = itemView.findViewById(R.id.textViewCartItemName)
        val textViewItemQuantity: TextView = itemView.findViewById(R.id.textViewCartItemQuantity)
        val textViewItemSubtotal: TextView = itemView.findViewById(R.id.textViewCartItemSubtotal)
        val imageButtonRemoveItem: ImageButton = itemView.findViewById(R.id.imageButtonRemoveItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position]

        // Obtener el nombre del producto usando el productId
        val productName = getProductName(item.productId)

        holder.textViewItemName.text = productName
        holder.textViewItemQuantity.text = "x${item.quantity}"
        val subtotal = item.quantity * item.price
        holder.textViewItemSubtotal.text = String.format("S/.%.2f", subtotal)

        // Configurar listener para el botón eliminar
        holder.imageButtonRemoveItem.setOnClickListener {
            onRemoveItemClick(item)
        }
    }

    override fun getItemCount() = cartItems.size

    // Función para obtener el nombre del producto por ID (similar a FavoritesActivity)
    private fun getProductName(productId: String): String {
        val allProducts = listOf(
            Product("chaufa_langostinos", "Chaufa Con Langostinos", "", 0), // Descripción e imagen no necesarias aquí
            Product("chaufa_carne", "Chaufa Con Carne", "", 0),
            Product("tallarines_pollo", "Tallarines Con Pollo", "", 0),
            Product("tallarines_carne", "Tallarines Con Carne", "", 0)
            // Asegúrate de que esta lista coincida con los productos de tu app
        )
        return allProducts.find { it.id == productId }?.name ?: "Producto Desconocido"
    }

    // Necesitarás la clase de datos Product si aún no está en un archivo compartido
    // data class Product(val id: String, val name: String, val description: String, val imageResId: Int)
} 