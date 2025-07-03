package com.example.fuxingapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.content.Intent

class PlatosAdapter(
    private var listaPlatos: MutableList<Plato>,
    private val favoritos: Set<String>,
    private val onFavoritoClick: (Plato) -> Unit
) : RecyclerView.Adapter<PlatosAdapter.PlatoViewHolder>() {

    private var platosOriginales: List<Plato> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlatoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_plato, parent, false)
        return PlatoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlatoViewHolder, position: Int) {
        holder.bind(listaPlatos[position])
    }

    override fun getItemCount(): Int = listaPlatos.size

    fun filterByNombre(nombre: String) {
        listaPlatos = if (nombre.isEmpty()) {
            platosOriginales.toMutableList()
        } else {
            platosOriginales.filter {
                it.nombre.lowercase().contains(nombre)
            }.toMutableList()
        }
        notifyDataSetChanged()
    }

    fun filterByCategoria(categoria: String?) {
        listaPlatos = if (categoria == null) {
            platosOriginales.toMutableList()
        } else {
            platosOriginales.filter { it.categoria.equals(categoria, true) }.toMutableList()
        }
        notifyDataSetChanged()
    }

    fun actualizarPlatos(nuevosPlatos: List<Plato>) {
        platosOriginales = nuevosPlatos
        listaPlatos = nuevosPlatos.toMutableList()
        notifyDataSetChanged()
    }


    inner class PlatoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imagenView: ImageView = itemView.findViewById(R.id.imagenView)
        private val nombreText: TextView = itemView.findViewById(R.id.nombreText)
        private val descripcionText: TextView = itemView.findViewById(R.id.descripcionText)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        private val favoritoIcon: ImageView = itemView.findViewById(R.id.favoritoIcon)

        fun bind(plato: Plato) {
            nombreText.text = plato.nombre
            descripcionText.text = plato.descripcion
            ratingBar.rating = plato.rating

            Glide.with(itemView.context)
                .load(plato.imagenUrl)
                .into(imagenView)

            val esFavorito = favoritos.contains(plato.id)
            favoritoIcon.setImageResource(
                if (esFavorito) R.drawable.ic_heart_filled_placeholder else R.drawable.ic_heart_outline_placeholder
            )

            favoritoIcon.setOnClickListener {
                onFavoritoClick(plato)
            }

            // Aquí abrimos la actividad del detalle al hacer clic en el ítem
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetallePlatoActivity::class.java)
                intent.putExtra("plato", plato)
                itemView.context.startActivity(intent)
            }
        }
    }
}