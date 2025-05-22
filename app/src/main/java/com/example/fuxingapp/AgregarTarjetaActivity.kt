package com.example.fuxingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Button
import android.content.Intent

class AgregarTarjetaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_tarjeta)

        val imageButtonBack: ImageButton = findViewById(R.id.imageButtonBack)
        imageButtonBack.setOnClickListener {
            onBackPressed() // Regresar a la actividad anterior
        }

        val buttonSaveCard: Button = findViewById(R.id.buttonSaveCard)
        buttonSaveCard.setOnClickListener {
            // Aquí iría la lógica para guardar los datos de la tarjeta
            // Una vez guardada (o si se decide no guardar), mostrar la notificación y cerrar esta actividad
            val intent = Intent(this, CardSavedNotificationActivity::class.java)
            startActivity(intent)
            finish() // Cierra esta actividad
        }

        // Aquí se añadiría la lógica para manejar los campos de entrada
    }
} 