package com.example.fuxingapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fuxingapp.R
import com.example.fuxingapp.util.CartManager
import com.example.fuxingapp.model.CartItem

class CartItemAdapter(
    private val cartItems: MutableList<CartItem>,
    private val onQuantityChanged: () -> Unit,
    private val onItemRemoved: () -> Unit
) : RecyclerView.Adapter<CartItemAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagePlato: ImageView = itemView.findViewById(R.id.imageViewCartItem)
        val textNombre: TextView = itemView.findViewById(R.id.textViewCartItemName)
        val textPrecioTotal: TextView = itemView.findViewById(R.id.textViewCartItemTotal)
        val textCantidad: TextView = itemView.findViewById(R.id.textViewCartItemQuantity)
        val btnIncrementar: ImageButton = itemView.findViewById(R.id.buttonIncrease)
        val btnDisminuir: ImageButton = itemView.findViewById(R.id.buttonDecrease)
        val btnEliminar: ImageButton = itemView.findViewById(R.id.buttonRemoveItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position]

        holder.textNombre.text = item.nombre
        holder.textCantidad.text = item.cantidad.toString()
        holder.textPrecioTotal.text = "S/. %.2f".format(item.precioTotal)

        Glide.with(holder.itemView.context)
            .load(item.imagenUrl)
            .placeholder(R.drawable.logo)
            .into(holder.imagePlato)

        holder.btnIncrementar.setOnClickListener {
            item.cantidad++
            CartManager.actualizarCantidad(item.id, item.cantidad)
            notifyItemChanged(position)
            onQuantityChanged()
        }

        holder.btnDisminuir.setOnClickListener {
            if (item.cantidad > 1) {
                item.cantidad--
                CartManager.actualizarCantidad(item.id, item.cantidad)
                notifyItemChanged(position)
                onQuantityChanged()
            }
        }

        holder.btnEliminar.setOnClickListener {
            val itemEliminado = cartItems.removeAt(position)
            CartManager.eliminarItem(itemEliminado)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, cartItems.size)
            onItemRemoved()
        }
    }

    override fun getItemCount(): Int = cartItems.size
}
