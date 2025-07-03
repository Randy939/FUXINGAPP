package com.example.fuxingapp

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fuxingapp.adapter.MensajeAdapter
import com.example.fuxingapp.model.Mensaje
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class CustomerServiceActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var mensajeAdapter: MensajeAdapter
    private val listaMensajes = mutableListOf<Mensaje>()

    private lateinit var editTextMensaje: EditText
    private lateinit var botonEnviar: ImageButton
    private lateinit var botonBack: ImageButton

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_service)

        recyclerView = findViewById(R.id.recyclerViewMessages)
        editTextMensaje = findViewById(R.id.editTextMessage)
        botonEnviar = findViewById(R.id.imageButtonSend)
        botonBack = findViewById(R.id.imageButtonBack)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Configurar RecyclerView
        mensajeAdapter = MensajeAdapter(listaMensajes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mensajeAdapter

        // Botón de regresar
        botonBack.setOnClickListener {
            finish()
        }

        // Botón de enviar
        botonEnviar.setOnClickListener {
            val texto = editTextMensaje.text.toString().trim()
            if (texto.isNotEmpty()) {
                enviarMensaje(texto)
                editTextMensaje.text.clear()
            }
        }

        // Cargar mensajes previos
        cargarMensajes()
    }

    private fun enviarMensaje(texto: String) {
        val mensaje = Mensaje(
            contenido = texto,
            enviadoPorUsuario = true,
            timestamp = System.currentTimeMillis()
        )

        val userId = auth.currentUser?.uid ?: return
        db.collection("Usuarios").document(userId)
            .collection("Mensajes")
            .add(mensaje)
            .addOnSuccessListener {
                listaMensajes.add(mensaje)
                mensajeAdapter.notifyItemInserted(listaMensajes.size - 1)
                recyclerView.scrollToPosition(listaMensajes.size - 1)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al enviar el mensaje", Toast.LENGTH_SHORT).show()
            }
    }

    private fun cargarMensajes() {
        val userId = auth.currentUser?.uid ?: return
        db.collection("Usuarios").document(userId)
            .collection("Mensajes")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { documentos ->
                listaMensajes.clear()
                for (doc in documentos) {
                    val mensaje = doc.toObject(Mensaje::class.java)
                    listaMensajes.add(mensaje)
                }
                mensajeAdapter.notifyDataSetChanged()
                recyclerView.scrollToPosition(listaMensajes.size - 1)
            }
    }
}
