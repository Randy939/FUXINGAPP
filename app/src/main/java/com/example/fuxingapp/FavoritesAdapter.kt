package com.example.fuxingapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fuxingapp.Product // Assuming Product is in this package
import com.google.android.material.card.MaterialCardView // Assuming you use MaterialCardView for the item layout

class FavoritesAdapter(
    private val productList: List<Product>,
    private val onItemClick: (Product) -> Unit // Lambda para manejar el clic del item
) : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewFood: ImageView = itemView.findViewById(R.id.imageViewFood)
        val textViewFoodName: TextView = itemView.findViewById(R.id.textViewFoodName)
        val textViewFoodDesc: TextView = itemView.findViewById(R.id.textViewFoodDesc)
        // Add other views from item_food_card.xml if needed for display,
        // e.g., val imageViewStar: ImageView = itemView.findViewById(R.id.imageViewStar)
        // val textViewRating: TextView = itemView.findViewById(R.id.textViewRating)
        // val imageButtonFavorite: ImageButton = itemView.findViewById(R.id.imageButtonFavorite) // Although favorite button might have its own listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food_card, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val currentProduct = productList[position]
        holder.imageViewFood.setImageResource(currentProduct.imageResId)
        holder.textViewFoodName.text = currentProduct.name
        holder.textViewFoodDesc.text = currentProduct.description

        // Set click listener on the item view
        holder.itemView.setOnClickListener {
            onItemClick(currentProduct) // Call the lambda with the clicked product
        }
    }

    override fun getItemCount() = productList.size
} 