package com.example.fuxingapp.util

import com.example.fuxingapp.model.CartItem

object CartManager {

    private val cartItems = mutableListOf<CartItem>()

    fun agregarAlCarrito(item: CartItem) {
        if (item.cantidad <= 0) return
        val existente = cartItems.find { it.id == item.id }
        if (existente != null) {
            existente.cantidad += item.cantidad
        } else {
            cartItems.add(item)
        }
    }


    fun obtenerItems(): List<CartItem> {
        return cartItems
    }

    fun eliminarItem(item: CartItem) {
        cartItems.removeIf { it.id == item.id }
    }

    fun vaciarCarrito() {
        cartItems.clear()
    }

    fun actualizarCantidad(itemId: String, nuevaCantidad: Int) {
        if (nuevaCantidad < 1) {
            cartItems.removeIf { it.id == itemId }
        } else {
            cartItems.find { it.id == itemId }?.cantidad = nuevaCantidad
        }
    }

    fun calcularTotal(): Double {
        return cartItems.sumOf { it.precioTotal }
    }

    fun estaVacio(): Boolean {
        return cartItems.isEmpty()
    }

    fun cantidadDeItems(): Int = cartItems.size

}
