package com.example.fuxingapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fuxingapp.R
import com.example.fuxingapp.model.Mensaje

class MensajeAdapter(private val mensajes: List<Mensaje>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TIPO_ENVIADO = 1
    private val TIPO_RECIBIDO = 2

    override fun getItemViewType(position: Int): Int {
        return if (mensajes[position].enviadoPorUsuario) TIPO_ENVIADO else TIPO_RECIBIDO
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TIPO_ENVIADO) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_sent, parent, false)
            MensajeEnviadoViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_received, parent, false)
            MensajeRecibidoViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mensaje = mensajes[position]
        if (holder is MensajeEnviadoViewHolder) {
            holder.textoMensaje.text = mensaje.contenido
        } else if (holder is MensajeRecibidoViewHolder) {
            holder.textoMensaje.text = mensaje.contenido
        }
    }

    override fun getItemCount(): Int = mensajes.size

    class MensajeEnviadoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textoMensaje: TextView = itemView.findViewById(R.id.textViewMessageSent)
    }

    class MensajeRecibidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textoMensaje: TextView = itemView.findViewById(R.id.textViewMessageReceived)
    }
}
