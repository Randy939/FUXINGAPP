package com.example.fuxingapp

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class CartItem(
    val productId: String,
    val quantity: Int,
    val price: Double // O Float, dependiendo de cómo manejes los precios
)

object CartManager {

    private const val PREFS_NAME = "cart_prefs"
    private const val CART_KEY = "cart_items"

    private val gson = Gson()

    fun addItemToCart(context: Context, item: CartItem) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()

        val currentItems = getCartItems(context).toMutableList()

        // Check if the item already exists in the cart (based on productId)
        val existingItemIndex = currentItems.indexOfFirst { it.productId == item.productId }

        if (existingItemIndex != -1) {
            // If item exists, update the quantity
            val existingItem = currentItems[existingItemIndex]
            val updatedItem = existingItem.copy(quantity = existingItem.quantity + item.quantity)
            currentItems[existingItemIndex] = updatedItem
        } else {
            // If item does not exist, add it to the list
            currentItems.add(item)
        }

        val json = gson.toJson(currentItems)
        editor.putString(CART_KEY, json)
        editor.apply()
    }

    fun getCartItems(context: Context): List<CartItem> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(CART_KEY, null)

        return if (json != null) {
            val type = object : TypeToken<List<CartItem>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    fun removeItemFromCart(context: Context, item: CartItem) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()

        val currentItems = getCartItems(context).toMutableList()

        // Find and remove the item based on productId (assuming unique product IDs)
        val itemToRemove = currentItems.find { it.productId == item.productId }
        if (itemToRemove != null) {
            currentItems.remove(itemToRemove)
        }

        val json = gson.toJson(currentItems)
        editor.putString(CART_KEY, json)
        editor.apply()
    }

    fun clearCart(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.remove(CART_KEY)
        editor.apply()
    }
} 